package ist.meic.pa;

public class TraceInfo {

	private String direction;
	private String method;
	private String filename;
	private int lineNumber;
	
	public TraceInfo() {
		
	}
	
	public TraceInfo(String direction, String method, String filename, int lineNumber) {
		this.direction = direction;
		this.method = method;
		this.filename = filename;
		this.lineNumber = lineNumber;
	}
	
	@Untraceable
	public String toString() {
		return this.direction + " " + this.method + " on " + this.filename + ":" + lineNumber;
	}

}
