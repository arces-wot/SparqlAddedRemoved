package model;

public class TestMetric {

	private long start=-1;
	private long end=-1;
	private String name;
	private boolean error = false;
	private boolean exc = false;
		
	public TestMetric(String name) {
		super();
		this.name = name;
	}
	
	public void start() {
		exc = true;
		start= System.currentTimeMillis();
	}
	
	public void stop(boolean err) {
		error=err;
		end= System.currentTimeMillis();
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
	
	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isExc() {
		return exc;
	}

	public void setExc(boolean exc) {
		this.exc = exc;
	}

	public long getInterval() {
		if(end<0 || start <0) {
			return -1;
		}
		return end-start;
	}
	
	public void print() {
		if(this.exc) {
			if(!this.error ) {
				System.out.println(name+ ": "+ getInterval() + "ms");
			}else {
				System.out.println(name+ ": FAILED");
			}
		}else {
			System.out.println(name+ ": not done");
		}
	}
	
	
}
