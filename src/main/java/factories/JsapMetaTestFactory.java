package factories;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import connector.SparqlRequest;
import core.IMetaSparqlRequest;
import core.MetaSparqlRequest;
import core.MetaTest;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.pattern.JSAP;
import model.EndPoint;
import model.SparqlObj;
import model.TripleBase;
import support.Environment;
import support.JSAPProvider;

public class JsapMetaTestFactory implements IMetaTestFactory{
	


	private static String builderBindInsert = "tripleBaseInsert";
	private static String builderBindDelete = "tripleBaseDelete";
	
	private static JsapMetaTestFactory instance=null;
		
	public static JsapMetaTestFactory getInstance() {
		if(instance==null) {
			try {
				instance=new JsapMetaTestFactory(new JSAPProvider().getAppProfile());
			} catch (SEPAPropertiesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SEPASecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}
	//--------------------------------------------------
	private String _host=Environment.host;
	private int _port=Environment.port;
	private String _protocol=Environment.protocol;
	private String _ontology=Environment.closeOntology;
	private String _graph=Environment.graph;
	private HashMap<String, MetaTest> metaTestMap = new HashMap<String, MetaTest>();
	
	private JsapMetaTestFactory(JSAP jsap) throws Exception {
		

		
		_host=jsap.getHost();
		_port=jsap.getPort();
		_protocol=jsap.getProtocolScheme();
		_ontology=jsap.getNamespaces().get("ub");
		_graph=jsap.getNamespaces().get("test");
		
		
		HashMap<String, MetaSparqlRequest> requestMap =getRequestsFromJsap(jsap);
		for (Entry<String, JsonElement> test : 	jsap.getExtendedData().get("tests").getAsJsonObject().entrySet()) {
			metaTestMap.put(test.getKey(), getFromJsap(test.getValue().getAsJsonObject(),requestMap,test.getKey()));
		} 
		

	}
	
	public Set<String> getTestNames() {
		// TODO Auto-generated method stub
		return metaTestMap.keySet();
	}

	public MetaTest getTestByName(String name) {
		// TODO Auto-generated method stub
		return metaTestMap.get(name);
	}
	
	private MetaTest getFromJsap(JsonObject obj,HashMap<String, MetaSparqlRequest> request,String name) throws Exception {	
		
		MetaTest ris = new MetaTest(
				request.get(obj.get("QueryLink").getAsString()),
				request.get(obj.get("UpdateLink").getAsString()),
				request.get(obj.get("RollbackLink").getAsString()),
				obj.get("AskTestEnable").getAsBoolean()
				);
		
		ris.setMetaTestName(name);
		if(obj.get("PreparationPercentage").getAsInt()>0 &&  obj.get("PreUpdateLink").getAsString()!=null && obj.get("PreUpdateRollbackLink").getAsString() !=null) {
			ris.setPreparation(
					request.get(obj.get("PreUpdateLink").getAsString()), 
					request.get(obj.get("PreUpdateRollbackLink").getAsString()),
					obj.get("PreparationPercentage").getAsInt()
					);
		} 
		
		ris.setPot(obj.get("Pot").getAsInt());
		ris.setReiteration(obj.get("Reiteration").getAsInt());
		
		return ris;
	}
	
	private HashMap<String, MetaSparqlRequest> getRequestsFromJsap(JSAP jsap){
		HashMap<String, MetaSparqlRequest> requestMap = new HashMap<String, MetaSparqlRequest>();
		String prefixs= "";
		for (String  ns : jsap.getNamespaces().keySet()) {
			prefixs+="PREFIX "+ ns + ": <"+jsap.getNamespaces().get(ns)+ ">\r\n";
		}

		for (String  id : jsap.getUpdateIds()) {
			String sparqlStr = prefixs+jsap.getSPARQLUpdate(id);
			SparqlObj sparql= new SparqlObj(sparqlStr) ;
			EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");	
			MetaSparqlRequest msr = new MetaSparqlRequest(new SparqlRequest(sparql,endPointHost));
			if(jsap.getUpdateBindings(id).getVariables().size()>0) {
				for (String bindVar : jsap.getUpdateBindings(id).getVariables()) {
					if(bindVar.compareTo(builderBindInsert)==0  ) {
						msr.setTripleInsert(new TripleBase( jsap.getUpdateBindings(id).getValue(bindVar)));
						msr.setBuilderBindInsert("?"+bindVar);
					}else if(bindVar.compareTo(builderBindDelete)==0 ) {
						msr.setTripleDelete( new TripleBase(jsap.getUpdateBindings(id).getValue(bindVar)));
						msr.setBuilderBindDelete("?"+bindVar);
					}else {
						System.out.println("Warning, in this Tester only bind as "+builderBindInsert+" and "+builderBindDelete+ " are allowed by Jsap.");
					}
				}
			}
			requestMap.put(id, msr);
		}
		for (String  id : jsap.getQueryIds()) {
			String sparqlStr = prefixs+jsap.getSPARQLQuery(id);
			SparqlObj sparql= new SparqlObj(sparqlStr) ;
			EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");	
			MetaSparqlRequest msr = new MetaSparqlRequest(new SparqlRequest(sparql,endPointHost));
			if(jsap.getQueryBindings(id).getVariables().size()>0) {
				for (String bindVar : jsap.getQueryBindings(id).getVariables()) {
					if(bindVar.compareTo(builderBindInsert)==0 ) {
						msr.setTripleInsert(new TripleBase( jsap.getQueryBindings(id).getValue(bindVar)));
						msr.setBuilderBindInsert("?"+bindVar);
					}else if(bindVar.compareTo(builderBindDelete)==0  ) {
						msr.setTripleDelete( new TripleBase(jsap.getQueryBindings(id).getValue(bindVar)));
						msr.setBuilderBindInsert("?"+bindVar);
					}else {
						System.out.println("Warning, in this Tester only bind as "+builderBindInsert+" and "+builderBindDelete+ " are allowed by Jsap.");
					}
				}
			}
			requestMap.put(id, msr);
		}
		return requestMap;
	}

}
