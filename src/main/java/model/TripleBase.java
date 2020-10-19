package model;

public class TripleBase {

	private String base_s;
	private String base_p;
	private String base_o;
	private String bind;
	private int inc;
	
	
	public TripleBase(String base_s, String base_p, String base_o) {
		super();
		this.base_s = base_s;
		this.base_p = base_p;
		this.base_o = base_o;
		this.bind = "__X__";
		this.inc=0;
	}
	
	public TripleBase(String base_s, String base_p, String base_o,String bind) {
		super();
		this.base_s = base_s;
		this.base_p = base_p;
		this.base_o = base_o;
		this.bind = bind;
		this.inc=0;
	}
	//------------------------------------------------
	
	public String getNextTriple() {
		String next =  this.inc+"";
		this.inc++;
		return base_s.replace(this.bind, next)+ " "+  base_p.replace(this.bind, next) + " "+  base_o.replace(this.bind, next); 
	}
	public void reset() {
		this.inc=0;
	}
	//--------------------------------------SETTERS GETTERS
	public String getBase_s() {
		return base_s;
	}
	public void setBase_s(String base_s) {
		this.base_s = base_s;
	}
	public String getBase_p() {
		return base_p;
	}
	public void setBase_p(String base_p) {
		this.base_p = base_p;
	}
	public String getBase_o() {
		return base_o;
	}
	public void setBase_o(String base_o) {
		this.base_o = base_o;
	}
	public int getInc() {
		return inc;
	}
	public void setInc(int inc) {
		this.inc = inc;
	}
	
	
}
