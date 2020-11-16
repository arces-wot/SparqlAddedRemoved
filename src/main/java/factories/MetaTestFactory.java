package factories;

import java.util.HashMap;
import java.util.Set;

import connector.SparqlRequest;
import core.request.MetaSparqlRequest;
import core.test.MetaTest;
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
		requestMap.put("InsertData",genericCreateMT("MT1","InsertData",true,10));
		requestMap.put("DeleteWhere",genericCreateMT("MT2","DeleteWhere",true,90));//need 100% of pre insered or at least 90
		requestMap.put("DeleteInsert",genericCreateMT("MT3","DeleteInsert",true,100));
		requestMap.put("DeleteData",genericCreateMT("MT4","DeleteData",true,90));//need 100% of pre insered or at least 90
	}
	
	public Set<String> getTestNames() {
		return requestMap.keySet();
	}

	public MetaTest getTestByName(String name) {
		return requestMap.get(name);
	}

	private MetaTest genericCreateMT(String mt,String name,boolean excAskTest,int preparationPercentage) {
		MetaRequestFactory instance = MetaRequestFactory.getInstance();
		MetaTest test = new MetaTest(
				(MetaSparqlRequest)instance.getRequestByName(mt+"_Query"),
				(MetaSparqlRequest)instance.getRequestByName(mt+"_Update"),
				(MetaSparqlRequest)instance.getRequestByName(mt+"_Rollback"),
				true
		);
		test.setMetaTestName(name+ " MetaTest");
		test.setExcAskTest(excAskTest);
		try {
			test.setPreparation(
					(MetaSparqlRequest)instance.getRequestByName(mt+"_prepare_insert"),
					(MetaSparqlRequest)instance.getRequestByName(mt+"_prepare_rollback"),
					preparationPercentage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return test; 
	}
	

	
}
