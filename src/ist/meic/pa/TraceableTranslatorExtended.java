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

public class TraceableTranslatorExtended implements Translator {

	public TraceableTranslatorExtended() {
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
				
			}

		}
	}

}
