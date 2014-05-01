package ist.meic.pa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trace {
	
	private static Map<ObjectWrapper, ArrayList<TraceInfo>> history = new HashMap<ObjectWrapper, ArrayList<TraceInfo>>();
	
	@Untraceable
	public static void addInfo(Object object, String direction, String method, String filename, int lineNumber) {
		TraceInfo info = new TraceInfo(direction, method, filename, lineNumber);
		ObjectWrapper obj = keyExists(object);
		if (obj != null) {
			history.get(obj).add(info);
		} else {
			obj = new ObjectWrapper(object);
			ArrayList<TraceInfo> lst = new ArrayList<TraceInfo>();
			lst.add(info);
			history.put(obj, lst);
		}
	}
	
	@Untraceable
	public static ObjectWrapper keyExists(Object object){
			for(ObjectWrapper o : history.keySet()){
				if(o.getObj() == object){
					return o;
				}
			}
		return null;
	}

	@Untraceable
	public static void print(Object object) {
		ObjectWrapper obj = keyExists(object);
		if (obj == null) {
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
