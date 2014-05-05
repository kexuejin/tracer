package ist.meic.pa;

public class TestExtended {
	
	String s = new String("Hello World");

	public TestExtended() {
		// TODO Auto-generated constructor stub
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
	
	public void test() {
		Trace.print(handle());
		Trace.print(giveMeString());
		Trace.print(modifyString());
	}

	public static void main(String[] args) {
		(new TestExtended()).test();

		

	}

}
