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
	private TripleBase update_roolbackT;
	private int preparationPercentage;
	private int reiteration;//reiteration of the same test
	private int pot; //power of two, number of test and number of test triple as: 2^x with x in[0;pot]
	private boolean needPreparation=false;
	private ITestVisitor monitor;
	public MetaTest(ITestVisitor monitor,SparqlRequest query,SparqlRequest update,SparqlRequest roolback,TripleBase update_roolbackTriple) {
		this.monitor=monitor;
		reiteration=1;
		pot=0;//2^0=1 --> 1 only test with 1 only triple
		this.update_roolbackT=update_roolbackTriple;
		test= new SingleTest(query, update, roolback);
	}
	
	public MetaTest(ITestVisitor monitor,SparqlRequest query,SparqlRequest update,SparqlRequest roolback,TripleBase update_roolbackTriple,int reiteration, int pot) {
		this(monitor,query,update,roolback,update_roolbackTriple);
		this.reiteration=reiteration;
		this.pot=pot;		
	}
	
	public void setPreparation(SparqlRequest preparation,SparqlRequest preparationRoolback,int preparationPercentage) throws Exception {
		if(preparationPercentage<0 || preparationPercentage>100) {
			throw new Exception("The percentage must be in range of 0-100, it is: "+preparationPercentage );
		}
		this.needPreparation=true;
		this.preparationPercentage=preparationPercentage;
		test.setPreparation(preparation, preparationRoolback);
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
		}	
		monitor.end();
		return new TestResult(singleTestsTime);
	}

	
	//----------------------------------ONLY GETTERS
	
	public SingleTest getTest() {
		return test;
	}

	public TripleBase getUpdate_roolbackTriples() {
		return update_roolbackT;
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
