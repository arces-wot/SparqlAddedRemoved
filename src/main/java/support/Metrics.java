package support;

import java.util.ArrayList;

import model.TestMetric;

public class Metrics {

	private static Metrics instance = null;
	public static Metrics getInstance(){
		if(instance==null) {
			instance= new Metrics();
		}
		return instance;
	}
	
	
	
	private ArrayList<TestMetric> fasi = new ArrayList<TestMetric>();
	
	public Metrics() {}
	
	public void addFase(TestMetric f) {
		fasi.add(f);
	}
	

	
}
