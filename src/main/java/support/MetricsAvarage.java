package support;

import java.util.ArrayList;

import model.TestMetric;

public class MetricsAvarage {

	private ArrayList<TestMetric> avarage = new ArrayList<TestMetric>();
	private ArrayList<Double> avarageTemps = new ArrayList<Double>();
	
	public MetricsAvarage(ArrayList<TestMetric> first){
		for (TestMetric testMetric : first) {
			TestMetric temp =new TestMetric("Avarage "+testMetric.getName());
			temp.setStart(0);
			avarage.add(temp);	
			avarageTemps.add(Double.valueOf(testMetric.getInterval()));
		}
	}

	public void add(ArrayList<TestMetric> list) {
		int actualSize = avarageTemps.size();
		for (int x =0; x<list.size();x++) {
			avarageTemps.set(x,((avarageTemps.get(x)*actualSize+list.get(x).getInterval())/actualSize+1.0));
		}
	}
	
	public ArrayList<TestMetric> finalizeAndGetAvarage(){
		for (int x =0; x<avarage.size();x++) {
			TestMetric temp = avarage.get(x);
			temp.setEnd( avarageTemps.get(x).longValue());
			avarage.set(x,temp);
		}
		return avarage;
	}
}
