package ist.meic.pa;

import javassist.ClassPool;
import javassist.Translator;
import javassist.tools.reflect.Loader;

public class TraceVMExtended {
	
	public static void main(String[] args) throws Throwable {
		Translator t = new TraceableTranslatorExtended();
		ClassPool p = ClassPool.getDefault();
		Loader classLoader = new Loader();
		classLoader.addTranslator(p, t);
		String[] restArgs = new String[args.length - 1];
		System.arraycopy(args, 1, restArgs, 0, restArgs.length);
		classLoader.run(args[0], restArgs);
	}
}
