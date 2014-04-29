package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

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
		final String template = 
				"TraceInfo info = new TraceInfo(\"%s\", \"%s\", \"%s\", %d);" +
				"Trace.addInfo($X, info);";
		
		for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
//			ctMethod.instrument(new ExprEditor() {
//				public void edit(FieldAccess fa) throws CannotCompileException {
//					
//				}
//			});
		}
	}

}
