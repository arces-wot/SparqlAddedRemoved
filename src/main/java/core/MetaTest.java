package core;

import java.util.ArrayList;

import connector.SparqlRequest;
import model.TestMetric;
import model.TestResult;
import model.TripleBase;
import support.MetricsAvarage;
import support.TestBuilder;

public class MetaTest implements ITest {

	private SingleTest test;
	private TripleBase tripleBase;
	private int preparationPercentage;
	private int reiteration;//reiteration of the same test
	private int pot; //power of two, number of test and number of test triple as: 2^x with x in[0;pot]
	private boolean needPreparation=false;
	private ITestVisitor monitor;
	
	public MetaTest(SparqlRequest query,SparqlRequest update,SparqlRequest rollback,TripleBase tripleBase,boolean askTestOn) {		
		reiteration=1;
		pot=0;//2^0=1 --> 1 only test with 1 only triple
		this.tripleBase=tripleBase;
		test= new SingleTest(query, update, rollback,askTestOn);
	}
	
	public MetaTest(SparqlRequest query,SparqlRequest update,SparqlRequest rollback,TripleBase tripleBase,boolean askTestOn,int reiteration, int pot) {
		this(query,update,rollback,tripleBase,askTestOn);
		this.reiteration=reiteration;
		this.pot=pot;		
	}
	
	public void setPreparation(SparqlRequest preparation,SparqlRequest preparationRollback,int preparationPercentage) throws Exception {
		if(preparationPercentage<0 || preparationPercentage>100) {
			throw new Exception("The percentage must be in range of 0-100, it is: "+preparationPercentage );
		}
		this.needPreparation=true;
		this.preparationPercentage=preparationPercentage;
		test.setPreparation(preparation, preparationRollback);
	}

	public TestResult execute() {
		int actualPot = 0;
		ArrayList<TestMetric> singleTestsTime = new ArrayList<TestMetric>();
		while(actualPot<=pot) {
			int n=(int) Math.pow(2, actualPot);
			monitor.start(n,reiteration);
			//-------------build Test
			SingleTest actualTest =TestBuilder.build(this, n);
			//-------------execute Test
			MetricsAvarage metricAvarage = null;
			TestMetric general = new TestMetric("Test with " + n + " Triples and " + reiteration + " reiteration.");
			general.start();
			for(int x=0;x<reiteration;x++) {
				TestResult partialResult = actualTest.execute();
				if(metricAvarage==null){
					metricAvarage=new MetricsAvarage(partialResult.getPhases());
				}else{
					metricAvarage.add(partialResult.getPhases());
				}
				//-------------------------------------WIP
				monitor.visit(partialResult);
			}
			general.stop();
			singleTestsTime.add(general);
			monitor.visit(metricAvarage.finalizeAndGetAvarage());
			actualPot++;
			monitor.end();
		}	
	
		return new TestResult(singleTestsTime);
	}


	//----------------------------------GETTERS and SETTERS
	public void setMonitor(ITestVisitor m) {
		this.monitor=m;
	}
	
	public void setReiteration(int reiteration) {
		this.reiteration = reiteration;
	}

	public void setPot(int pot) {
		this.pot = pot;
	}

	public SingleTest getTest() {
		return test;
	}

	public TripleBase getTriples() {
		return tripleBase;
	}

	public int getPreparationPercentage() {
		return preparationPercentage;
	}

	public int getReiteration() {
		return reiteration;
	}

	public int getPot() {
		return pot;
	}

	public boolean isNeedPreparation() {
		return needPreparation;
	}
	
	
	
}
