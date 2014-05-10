package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.expr.Cast;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.Handler;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

public class TraceableTranslatorExtended implements Translator {

	public TraceableTranslatorExtended() {
	}

	@Override
	public void onLoad(ClassPool pool, String className)
			throws NotFoundException, CannotCompileException {
		CtClass ctClass = pool.get(className);
		makeTraceable(ctClass);
	}

	@Override
	public void start(ClassPool pool) throws NotFoundException,
			CannotCompileException {

	}

	private void makeTraceable(CtClass ctClass) throws NotFoundException,
			CannotCompileException {
		final String template = "ist.meic.pa.Trace.addInfo($%s, \"%s\", \"%s\", \"%s\", %d);";
		final String castTemplate = "ist.meic.pa.Trace.addInfo($%s, \"%s\", \"(%s) cast in %s\", \"%s\", %d);";
		if (!ctClass.hasAnnotation(Untraceable.class)) {
			for (CtBehavior bv : ctClass.getDeclaredBehaviors()) {
				bv.instrument(new ExprEditor() {
					public void edit(MethodCall mc) throws CannotCompileException {
						if (!isANativeMethod(mc)) {
							try {
								CtMethod mth = mc.getMethod();
								String addedInfo = "";
								addedInfo += addParametersToTracer(template,
										mc, mth);
								addedInfo += addReturnedToTrace(template, mc,
										mth);
								mc.replace(addedInfo);
							} catch (NotFoundException e) {
								e.printStackTrace();
							}
						}
					}

					private boolean isANativeMethod(MethodCall mc) {
						return mc.getClassName().split("\\.")[0].equals("java");
					}

					private String addReturnedToTrace(final String template,
							MethodCall mc, CtMethod mth)
							throws NotFoundException {
						String addedInfo = "";
						CtClass returnType = mth.getReturnType();
						if (!returnType.getName().equals("void") && !returnType.isPrimitive()) {
							addedInfo = String.format(template, "_", "<-",
									mth.getLongName(), mc.getFileName(),
									mc.getLineNumber());
						}
						return addedInfo;
					}

					private String addParametersToTracer(final String template,
							MethodCall mc, CtMethod mth)
							throws NotFoundException {
						int args = mth.getParameterTypes().length;
						String addedInfo = "";
						for (int i = 0; i < args; i++) {
							if (!mth.getParameterTypes()[i].isPrimitive()) {
								addedInfo += String.format(template, i + 1,
										"->", mth.getLongName(),
										mc.getFileName(), mc.getLineNumber());
							}
						}
						addedInfo += "$_ = $proceed($$);";
						return addedInfo;
					}

					public void edit(NewExpr ne) throws CannotCompileException {
						if (!ne.getEnclosingClass().getName()
								.equals("ist.meic.pa.Trace")) {
							try {
								CtConstructor cons = ne.getConstructor();
								String addedInfo = "$_ = $proceed($$);"
										+ String.format(template, "_", "<-",
												cons.getLongName(),
												ne.getFileName(),
												ne.getLineNumber());
								ne.replace(addedInfo);
							} catch (NotFoundException e) {
								e.printStackTrace();
							}
						}

					}

					public void edit(Handler h) throws CannotCompileException {
						h.insertBefore(String.format(template, "1", "=>", h
								.where().getLongName(), h.getFileName(), h
								.getLineNumber()));
					}

					public void edit(Cast h) throws CannotCompileException {
						String addedInfo;
						try {
							addedInfo = "$_ = $proceed($$);"
									+ String.format(castTemplate, "_", "()", h
											.getType().getName(), h.where()
											.getLongName(), h.getFileName(), h
											.getLineNumber());
							h.replace(addedInfo);
						} catch (NotFoundException e) {
							e.printStackTrace();
						}
					}

					public void edit(FieldAccess h)
							throws CannotCompileException {
						try {
							if (!h.getField().getType().isPrimitive()) {
								if (h.isReader()) {
									String addedInfo = "$_ = $proceed($$);"
											+ String.format(
													template,
													"_",
													"<.",
													h.getFieldName()
															+ " in "
															+ h.where()
																	.getLongName(),
													h.getFileName(), h
															.getLineNumber());
									h.replace(addedInfo);
								} else if (h.isWriter()) {
									String addedInfo = "$_ = $proceed($$);"
											+ String.format(template, "1", ".>",
													h.getFieldName() + " in " + h.where().getLongName(), 
													h.getFileName(), h.getLineNumber()); h.replace(addedInfo);
								}
							}
						} catch (NotFoundException e1) {
							e1.printStackTrace();
						}
					}
				});

			}
		}
	}

}
