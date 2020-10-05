package addedremoved.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import addedremoved.AddedRemovedGenerator;
import connector.IRequestFactory;
import connector.ISparqlRequest;
import connector.RequestFactory;
import connector.RequestFactory.RequestName;
import connector.SparqlRequest;
import core.Inspector;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import it.unibo.arces.wot.sepa.commons.response.Response;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import model.TestMetric;
import model.UpdateConstruct;
import support.Metrics;

public class AddedRemovedTest {

	private static IRequestFactory factory = null;
	private static Metrics metrics = null;
	@BeforeClass
	public static void init() {
		//instanzio la fattoria di richieste
		factory=RequestFactory.getInstance();
		//e il raccoglitore delle metriche
		metrics=Metrics.getInstance();
	}

	
	@Test // (timeout = 5000)
	public void test_Q2() {

		SparqlRequest update_for_Q2=(SparqlRequest)factory.getRequestByName(RequestName.UPDATE_FOR_Q2.toString());
		SparqlRequest query_Q2=(SparqlRequest)factory.getRequestByName(RequestName.QUERY2.toString());
		SparqlRequest roolback_for_Q2=(SparqlRequest)factory.getRequestByName(RequestName.ROLLBACK_FOR_Q2.toString());
		SparqlRequest deleteUpdate=null;	
		SparqlRequest insertUpdate=null;
		
		//----------------------------------Phase 1
		TestMetric Phase1 = new TestMetric("Added removed extraction and generation of updates (insert and delete)");		
		
		Phase1.start();
		UpdateConstruct constructs = AddedRemovedGenerator.getAddedRemovedFrom(update_for_Q2.clone());
		if(constructs.needDelete()) {
			try {
				deleteUpdate =AddedRemovedGenerator.generateDeleteUpdate(update_for_Q2.clone(),constructs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(constructs.needInsert()) {
			try {
				insertUpdate =AddedRemovedGenerator.generateInsertUpdate(update_for_Q2.clone(),constructs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Phase1.stop();
		
	    assertFalse("#)  Phase1",constructs==null || (deleteUpdate==null && insertUpdate==null) );
	    
		//----------------------------------Phase 2	    
		TestMetric Phase2 = new TestMetric("Execution query");
		
		Phase2.start();
		Response pre_ris_query = query_Q2.execute();
		Phase2.stop();
		
		assertFalse("#)  Phase2",pre_ris_query.isError());
		
		//----------------------------------Phase 3	    
		TestMetric Phase3 = new TestMetric("Execution normal update");
		Phase3.start();
		Response ris_update = update_for_Q2.execute();
		Phase3.stop();
		
		assertFalse("#)  Phase3",ris_update.isError());
		
		//----------------------------------Phase 4	    
		TestMetric Phase4 = new TestMetric("Execution Query N°2 1/2");
		
		Phase4.start();
		QueryResponse ris_Query = (QueryResponse)query_Q2.execute();
		Phase4.stop();
		
		assertFalse("#)  Phase4",ris_Query.isError());
		

		//----------------------------------Phase 5	    
		TestMetric Phase5 = new TestMetric("Execution RoolBack Update");
		
		Phase5.start();
		Response ris_Roolback =roolback_for_Q2.execute();
		Phase5.stop();
		
		assertFalse("#)  Phase5",ris_Query.isError());
	
		
		//----------------------------------Phase 6	    
		TestMetric Phase6 = new TestMetric("Execution insert and delete");
		Response ris_insert =null;
		Response ris_delete =null;
		
		Phase6.start();
		if(insertUpdate!=null) {
			ris_insert =insertUpdate.execute();
		}
		if(deleteUpdate!=null) {
			ris_delete =deleteUpdate.execute();
		}
		Phase6.stop();
		
		if(ris_insert!=null){
			assertFalse("#)  Phase6 insert success",ris_insert.isError());
		}
		if(ris_delete!=null){
			assertFalse("#)  Phase6 delete success",ris_delete.isError());
		}
		
		if(ris_insert==null && ris_delete==null) {
			System.out.println("Warning both insert and delete updates are void!");
		}
		
		//----------------------------------Phase 7	    
		TestMetric Phase7 = new TestMetric("Execution Query N°2 2/2");
		
		
		Phase7.start();
		QueryResponse ris_Query_2 = (QueryResponse)query_Q2.execute();
		Phase7.stop();
		
		assertFalse("#)  Phase7",ris_Query_2.isError());
		
		//----------------------------------Phase 8	
		assertTrue("#)  Phase8 Querys resutl (need be equasl)",Inspector.areEq(ris_Query_2.getBindingsResults(),ris_Query.getBindingsResults()));
		System.out.println("--------------------------------------------------------------TEST 1");
		System.out.println("---------------------------------------------------------");
		System.out.println("------------Query after normal update result-------------");
		System.out.println("---------------------------------------------------------");
		System.out.println(ris_Query.getBindingsResults().toJson().toString());
		System.out.println("---------------------------------------------------------");
		System.out.println("------------Query after insert delete updates------------");
		System.out.println("---------------------------------------------------------");
		System.out.println(ris_Query_2.getBindingsResults().toJson().toString());
		System.out.println("---------------------------------------------------------");
		System.out.println("----------------------Phases Times-----------------------");
		System.out.println("---------------------------------------------------------");
		
		Phase1.print();
		Phase2.print();
		Phase3.print();
		Phase4.print();
		Phase5.print();
		Phase6.print();
		Phase7.print();
		System.out.println("");
		
	}

}
