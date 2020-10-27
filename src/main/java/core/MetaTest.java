package core;

import java.util.ArrayList;

import connector.SparqlRequest;
import model.TestMetric;
import model.TestResult;
import model.TripleBase;
import support.MetricsAvarage;
import support.TestBuilder;

public class MetaTest implements ITest {

	private MetaSparqlRequest preparationInsert=null;
	private MetaSparqlRequest query=null;
	private MetaSparqlRequest update=null;
	private MetaSparqlRequest rollback=null;
	private MetaSparqlRequest rollbackPreparation=null;
	
	private int preparationPercentage;
	private int reiteration;//reiteration of the same test
	private int pot; //power of two, number of test and number of test triple as: 2^x with x in[0;pot]
	private boolean needPreparation=false;
	private ITestVisitor monitor;
	private String metaTestName ="GenericMetaTest";
	
	/*
	 * excAskTest need to be false if:
	 * 1) The update insert or delete triples aren't catch at all by the query
	 * 2) The query select pattern is not equals to that triples
	 */
	private boolean excAskTest;
	
	
	public MetaTest(MetaSparqlRequest query,MetaSparqlRequest update,MetaSparqlRequest rollback,boolean askTestOn) {		
		reiteration=1;
		pot=0;//2^0=1 --> 1 only test with 1 only triple
		this.query=query;
		this.update=update;
		this.rollback=rollback;
		this.excAskTest=askTestOn;
	}
	
	public MetaTest(MetaSparqlRequest query,MetaSparqlRequest update,MetaSparqlRequest rollback,boolean askTestOn,int reiteration, int pot) {
		this(query,update,rollback,askTestOn);
		this.reiteration=reiteration;
		this.pot=pot;		
	}
	
	public void setPreparation(MetaSparqlRequest preparation,MetaSparqlRequest preparationRollback,int preparationPercentage) throws Exception {
		if(preparationPercentage<0 || preparationPercentage>100) {
			throw new Exception("The percentage must be in range of 0-100, it is: "+preparationPercentage );
		}
		this.needPreparation=true;
		this.preparationPercentage=preparationPercentage;
		this.preparationInsert=preparation;
		this.rollbackPreparation=preparationRollback;
	}

	public TestResult execute() {
		int actualPot = 0;
		ArrayList<TestMetric> singleTestsTime = new ArrayList<TestMetric>();
		while(actualPot<=pot) {
			int n=(int) Math.pow(2, actualPot);
			if(monitor!=null) {
				monitor.start(n,reiteration,metaTestName,preparationPercentage);
			}
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
				if(monitor!=null) {
					monitor.visit(partialResult);
				}
			}
			general.stop();
			singleTestsTime.add(general);
			if(monitor!=null) {
				monitor.visit(metricAvarage.finalizeAndGetAvarage());
				monitor.end();
			}
			actualPot++;
		}	
	
		return new TestResult(singleTestsTime);
	}


	//----------------------------------GETTERS and SETTERS
	
	
	
	
	public void setMonitor(ITestVisitor m) {
		this.monitor=m;
	}
	
	public MetaSparqlRequest getQuery() {
		return query;
	}

	public void setQuery(MetaSparqlRequest query) {
		this.query = query;
	}

	public MetaSparqlRequest getUpdate() {
		return update;
	}

	public void setUpdate(MetaSparqlRequest update) {
		this.update = update;
	}

	public MetaSparqlRequest getRollback() {
		return rollback;
	}

	public void setRollback(MetaSparqlRequest rollback) {
		this.rollback = rollback;
	}

	public boolean isExcAskTest() {
		return excAskTest;
	}

	public void setExcAskTest(boolean excAskTest) {
		this.excAskTest = excAskTest;
	}

	public MetaSparqlRequest getPreparationInsert() {
		return preparationInsert;
	}

	public MetaSparqlRequest getRollbackPreparation() {
		return rollbackPreparation;
	}

	public String getMetaTestName() {
		return metaTestName;
	}

	public void setMetaTestName(String metaTestName) {
		this.metaTestName = metaTestName;
	}

	public void setReiteration(int reiteration) {
		this.reiteration = reiteration;
	}

	public void setPot(int pot) {
		this.pot = pot;
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
		return needPreparation && preparationInsert!=null && rollbackPreparation!=null;
	}
	
	
	
}
