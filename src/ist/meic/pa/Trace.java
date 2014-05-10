package ist.meic.pa;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
@Untraceable
public class Trace {
	
	private static Map<Object, ArrayList<TraceInfo>> history = new IdentityHashMap<Object, ArrayList<TraceInfo>>();
	
	
	public static void addInfo(Object object, String direction, String method, String filename, int lineNumber) {
		TraceInfo info = new TraceInfo(direction, method, filename, lineNumber);
		if (history.containsKey(object)) {
			history.get(object).add(info);
		} else {
			ArrayList<TraceInfo> lst = new ArrayList<TraceInfo>();
			lst.add(info);
			history.put(object, lst);
		}
	}

	
	public static void print(Object object) {
		if (!history.containsKey(object)) {
			System.err.println("Tracing for " + object + " is nonexistent!");
		} else {
			System.err.println("Tracing for " + object);
			ArrayList<TraceInfo> info = history.get(object);
			
			for (TraceInfo i : info) {
				System.err.println("  " + i);
			}
		}
	}
}
