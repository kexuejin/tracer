package ist.meic.pa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trace {
	
	private static Map<ObjectWrapper, ArrayList<TraceInfo>> history = new HashMap<ObjectWrapper, ArrayList<TraceInfo>>();
	
	@Untraceable
	public static void addInfo(Object object, String direction, String method, String filename, int lineNumber) {
		TraceInfo info = new TraceInfo(direction, method, filename, lineNumber);
		ObjectWrapper obj = new ObjectWrapper(object);
		if (history.containsKey(obj)) {
			history.get(obj).add(info);
		} else {
			ArrayList<TraceInfo> lst = new ArrayList<TraceInfo>();
			lst.add(info);
			history.put(obj, lst);
		}
	}

	@Untraceable
	public static void print(Object object) {
		ObjectWrapper obj = new ObjectWrapper(object);
		if (!history.containsKey(obj)) {
			System.err.println("Tracing for " + object + " is nonexistent!");
		} else {
			System.err.println("Tracing for " + object);
			ArrayList<TraceInfo> info = history.get(obj);
			
			for (TraceInfo i : info) {
				System.err.println("  " + i);
			}
		}
	}
}
