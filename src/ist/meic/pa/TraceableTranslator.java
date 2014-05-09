package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

public class TraceableTranslator implements Translator {

	public TraceableTranslator() {
	}

	@Override
	public void onLoad(ClassPool pool, String className) throws NotFoundException,
			CannotCompileException {
		CtClass ctClass = pool.get(className);
		makeTraceable(ctClass);
	}

	@Override
	public void start(ClassPool pool) throws NotFoundException,
			CannotCompileException {

	}
	
	private void makeTraceable(CtClass ctClass) throws NotFoundException, CannotCompileException {
		final String template = "ist.meic.pa.Trace.addInfo($%s, \"%s\", \"%s\", \"%s\", %d);";
		for (CtBehavior bv : ctClass.getDeclaredBehaviors()) {
			if (!bv.hasAnnotation(Untraceable.class)) {
				bv.instrument(new ExprEditor() {
					public void edit(MethodCall mc) throws CannotCompileException {
						if (!mc.getClassName().split("\\.")[0].equals("java")) {
							try {
								CtMethod mth = mc.getMethod();
								int args = mth.getParameterTypes().length;
								String addedInfo = "";
								addedInfo += addParametersToTracer(template,
										mc, mth, args);
								addedInfo += addReturnedToTrace(template, mc,
										mth);
								mc.replace(addedInfo);
							} catch (NotFoundException e) {
								e.printStackTrace();
							}
						}
					}

					private String addReturnedToTrace(final String template,
							MethodCall mc, CtMethod mth)
							throws NotFoundException {
						String addedInfo = "";
						if (!mth.getReturnType().getName().equals("void")) {
							addedInfo = String.format(template, "_", "<-",
									mth.getLongName(), mc.getFileName(),
									mc.getLineNumber());
						}
						return addedInfo;
					}

					private String addParametersToTracer(final String template, MethodCall mc, CtMethod mth, int args) {
						String addedInfo = "";
						for (int i = 0; i < args; i++) {
							addedInfo += String.format(template, i + 1, "->", mth.getLongName(), mc.getFileName(), mc.getLineNumber());
						}
						addedInfo += "$_ = $proceed($$);";
						return addedInfo;
					}

					public void edit(NewExpr ne) throws CannotCompileException {
						if (!ne.getEnclosingClass().getName().equals("ist.meic.pa.Trace")) {
							try {
								CtConstructor cons = ne.getConstructor();
								String addedInfo = "$_ = $proceed($$);" + String.format(template, "_", "<-", cons.getLongName(), ne.getFileName(), ne.getLineNumber());
								ne.replace(addedInfo);
							} catch (NotFoundException e) {
								e.printStackTrace();
							}
						}

					}
				});

			}
		}
	}

}
