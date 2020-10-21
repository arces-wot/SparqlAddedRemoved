package factories;

import java.util.HashMap;
import java.util.Set;

import connector.SparqlRequest;
import core.MetaSparqlRequest;
import core.MetaTest;
import model.TripleBase;

public class MetaTestFactory implements IMetaTestFactory {
	private static MetaTestFactory instance=null;
	
	
	public static MetaTestFactory getInstance() {
		if(instance==null) {
			instance=new MetaTestFactory();
		}
		return instance;
	}
	

	private HashMap<String, MetaTest> requestMap = new HashMap<String, MetaTest>();
	
	public MetaTestFactory() {
		requestMap.put("InsertData",generateOnlyInsertMetaTest());
		requestMap.put("DeleteWhere",generateOnlyDeleteMetaTest());
		requestMap.put("DeleteInsert",generateUpdateMetaTest());
	}
	
	public Set<String> getTestNames() {
		return requestMap.keySet();
	}

	public MetaTest getTestByName(String name) {
		return requestMap.get(name);
	}

	
	private MetaTest generateOnlyInsertMetaTest() {
		MetaRequestFactory instance = MetaRequestFactory.getInstance();
		MetaTest test = new MetaTest(
				(MetaSparqlRequest)instance.getRequestByName("MT1_Query"),
				(MetaSparqlRequest)instance.getRequestByName("MT1_Update"),
				(MetaSparqlRequest)instance.getRequestByName("MT1_Rollback"),
				true
		);
		test.setMetaTestName("Insert MetaTest");
		test.setExcAskTest(true);
		try {
			test.setPreparation(
					(MetaSparqlRequest)instance.getRequestByName("MT1_prepare_insert"),
					(MetaSparqlRequest)instance.getRequestByName("MT1_prepare_rollback"),
					10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return test; 
	}
	
	private MetaTest generateOnlyDeleteMetaTest() {
		MetaRequestFactory instance = MetaRequestFactory.getInstance();
		MetaTest test = new MetaTest(
				(MetaSparqlRequest)instance.getRequestByName("MT2_Query"),
				(MetaSparqlRequest)instance.getRequestByName("MT2_Update"),
				(MetaSparqlRequest)instance.getRequestByName("MT2_Rollback"),
				true
		);
		test.setMetaTestName("Delete MetaTest");
		test.setExcAskTest(true);
		try {
			test.setPreparation(
					(MetaSparqlRequest)instance.getRequestByName("MT2_prepare_insert"),
					(MetaSparqlRequest)instance.getRequestByName("MT2_prepare_rollback"),
					90);//need 100% of pre insered or at least 90
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return test; 
	}
	
	private MetaTest generateUpdateMetaTest() {
		MetaRequestFactory instance = MetaRequestFactory.getInstance();
		MetaTest test = new MetaTest(
				(MetaSparqlRequest)instance.getRequestByName("MT3_Query"),
				(MetaSparqlRequest)instance.getRequestByName("MT3_Update"),
				(MetaSparqlRequest)instance.getRequestByName("MT3_Rollback"),
				true
		);
		test.setMetaTestName("Update MetaTest");
		test.setExcAskTest(true);
		try {
			test.setPreparation(
					(MetaSparqlRequest)instance.getRequestByName("MT3_prepare_insert"),
					(MetaSparqlRequest)instance.getRequestByName("MT3_prepare_rollback"),
					100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return test; 
	}
	
}
