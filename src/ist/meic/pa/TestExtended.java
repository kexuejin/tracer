package ist.meic.pa;

public class TestExtended {
	
	String s = new String("Hello World");
	
	public TestExtended() {
	}
	
	public String giveMeString(){
		return s;
	}
	
	public String modifyString(){
		this.s = new String("Goodbye Cruel World!");
		return s;
	}
	
	public Object handle() {
		
		try {
			handleAgain();
		} catch (Exception e) {
			foo(e);
			return e;
		}
		
		return null;
	}
	
	public void foo(Exception e) {
		System.out.println(e.getMessage());
	}
	
	public void handleAgain() throws Exception {
		try {
			throw new Exception("Random exception");
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Object cast() {
		Object o = "lol";
		return (String) o;
	}
	
	public Object aniCast() {
		Object a = new Dog();
		return (Animal) a;
	}
	
	public void test() {
		Trace.print(handle());
		Trace.print(giveMeString());
		Trace.print(modifyString());
		Trace.print(cast());
		Trace.print(aniCast());
	}

	public static void main(String[] args) {
		(new TestExtended()).test();

		

	}

}
