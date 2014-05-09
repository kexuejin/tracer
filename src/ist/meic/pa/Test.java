package ist.meic.pa;

public class Test {
	
	public Object field = bar();
	public Object a = new Animal();

	public Test() {
		// TODO Auto-generated constructor stub
	}
	
	public Object foo() {
		String s = new String("Foo");
		return identity(s);
	}
	
	public Object bar() {
		return foo();
	}
	
	public Object baz() {
		return bar();
	}
	
	public Object identity(Object o) {
		return o;
	}
	
	public void test() {
		Trace.print(a);
		Trace.print(foo());
		Trace.print(bar());
		Trace.print(baz());
	}

	public static void main(String[] args) {
		(new Test()).test();

	}

}
