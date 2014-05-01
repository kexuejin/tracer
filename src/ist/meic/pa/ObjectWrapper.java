package ist.meic.pa;

public class ObjectWrapper {
	
	private Object obj;
	
	public ObjectWrapper(Object obj) {
		this.obj = obj;
	}
	
	@Untraceable
	public Object getObj() {
		return obj;
	}

}
