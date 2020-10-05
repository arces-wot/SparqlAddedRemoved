package model;

public class TestMetric {

	private long start;
	private long end;
	private String name;
	
		
	public TestMetric(String name) {
		super();
		this.name = name;
	}
	
	public void start() {
		start= System.currentTimeMillis();
	}
	
	public void stop() {
		end= System.currentTimeMillis();
	}
	
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void print() {
		System.out.println(name+ ": "+ (end-start)+ "ms");
	}
	
	
}
