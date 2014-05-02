package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.ClassPool;
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
		for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
			if (!ctMethod.hasAnnotation(Untraceable.class)) {
				ctMethod.instrument(new ExprEditor() {
					public void edit(MethodCall mc) throws CannotCompileException {
						int args;
						CtMethod mth;
						try {
							mth = mc.getMethod();
							args = mth.getParameterTypes().length;
							String addedInfo = "";
							for (int i = 0; i < args; i++) {
								addedInfo += String.format(template, i + 1, "->", mth.getLongName(), mc.getFileName(), mc.getLineNumber());
							}
							addedInfo += "$_ = $proceed($$);";
							if (!mth.getReturnType().getName().equals("void")) {
								addedInfo += String.format(template, "_", "<-", mth.getLongName(), mc.getFileName(), mc.getLineNumber());
							}
							mc.replace(addedInfo);
						} catch (NotFoundException e) {
							e.printStackTrace();
						} 

					}

					public void edit(NewExpr ne) throws CannotCompileException {
						CtConstructor cons;
						try {
							cons = ne.getConstructor();
							String addedInfo = "$_ = $proceed($$);" +
									String.format(template, "_", "<-", cons.getLongName(), ne.getFileName(), ne.getLineNumber());
							ne.replace(addedInfo);
						} catch (NotFoundException e) {
							e.printStackTrace();
						}
					}
				});
			}

		}
	}

}
