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
	
	@Untraceable
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectWrapper){
			ObjectWrapper o = (ObjectWrapper) obj;
			return o.getObj() == this.obj;
		}
		return false;
	}
	
	@Untraceable
	@Override
	public int hashCode() {
		return obj.hashCode();
	}

}
