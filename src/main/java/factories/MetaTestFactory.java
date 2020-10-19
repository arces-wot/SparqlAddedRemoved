package factories;

import java.util.HashMap;
import java.util.Set;

import connector.SparqlRequest;
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
		requestMap.put("InsertOnly",generateOnlyInsertMetaTest());
		requestMap.put("DeleteOnly",generateOnlyDeleteMetaTest());
		requestMap.put("Update",generateUpdateMetaTest());
	}
	
	public Set<String> getTestNames() {
		return requestMap.keySet();
	}

	public MetaTest getTestByName(String name) {
		return requestMap.get(name);
	}

	
	private MetaTest generateOnlyInsertMetaTest() {
		RequestFactoryForMetaTest instance = RequestFactoryForMetaTest.getInstance();
		MetaTest test = new MetaTest(
				(SparqlRequest)instance.getRequestByName("MT1_Query"),
				(SparqlRequest)instance.getRequestByName("MT1_Update"),
				(SparqlRequest)instance.getRequestByName("MT1_Rollback"),
				instance.getTripleBaseByName("MT1"),
				true
		);
		try {
			test.setPreparation(
					(SparqlRequest)instance.getRequestByName("MT1_prepare_insert"),
					(SparqlRequest)instance.getRequestByName("MT1_prepare_rollback"),
					10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return test; 
	}
	
	private MetaTest generateOnlyDeleteMetaTest() {
		return null; //WIP
	}
	
	private MetaTest generateUpdateMetaTest() {
		return null; //WIP
	}
	
}
