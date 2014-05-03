package ist.meic.pa;

public class TestExtended {

	public TestExtended() {
		// TODO Auto-generated constructor stub
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
	}

	public static void main(String[] args) {
		(new TestExtended()).test();

	}

}
