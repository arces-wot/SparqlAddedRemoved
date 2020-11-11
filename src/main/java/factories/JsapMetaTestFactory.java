package factories;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import connector.SparqlRequest;
import core.request.JsapMetaSparqlRequest;
import core.test.MetaTest;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.pattern.JSAP;
import model.EndPoint;
import model.SparqlObj;
import model.TripleBase;
import support.Environment;
import support.JSAPProvider;

public class JsapMetaTestFactory implements IMetaTestFactory{
	

	
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
		
		
		HashMap<String, JsapMetaSparqlRequest> requestMap =getRequestsFromJsap(jsap);
		for (Entry<String, JsonElement> test : 	jsap.getExtendedData().get("tests").getAsJsonObject().entrySet()) {
			metaTestMap.put(test.getKey(), getFromJsap(test.getValue().getAsJsonObject(),requestMap,test.getKey()));
		} 
		

	}
	
	
	private MetaTest getFromJsap(JsonObject obj,HashMap<String, JsapMetaSparqlRequest> request,String name) throws Exception {	
		
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
	
	private HashMap<String, JsapMetaSparqlRequest> getRequestsFromJsap(JSAP jsap){
		HashMap<String, JsapMetaSparqlRequest> requestMap = new HashMap<String, JsapMetaSparqlRequest>();
		String prefixs= "";
		for (String  ns : jsap.getNamespaces().keySet()) {
			prefixs+="PREFIX "+ ns + ": <"+jsap.getNamespaces().get(ns)+ ">\r\n";
		}

		for (String  id : jsap.getUpdateIds()) {
			String sparqlStr = prefixs+jsap.getSPARQLUpdate(id);
			SparqlObj sparql= new SparqlObj(sparqlStr) ;
			EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");	
			HashMap<String,TripleBase> forceBinds = new HashMap<String,TripleBase>();
			if(jsap.getUpdateBindings(id).getVariables().size()>0) {
				for (String bindVar : jsap.getUpdateBindings(id).getVariables()) {	
					forceBinds.put("?"+bindVar, new TripleBase( jsap.getUpdateBindings(id).getValue(bindVar)));
				}
			}
			requestMap.put(id, new JsapMetaSparqlRequest(new SparqlRequest(sparql,endPointHost),forceBinds));
		}
		for (String  id : jsap.getQueryIds()) {
			String sparqlStr = prefixs+jsap.getSPARQLQuery(id);
			SparqlObj sparql= new SparqlObj(sparqlStr) ;
			EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");	
			HashMap<String,TripleBase> forceBinds = new HashMap<String,TripleBase>();
			if(jsap.getQueryBindings(id).getVariables().size()>0) {
				for (String bindVar : jsap.getQueryBindings(id).getVariables()) {
					forceBinds.put("?"+bindVar, new TripleBase( jsap.getUpdateBindings(id).getValue(bindVar)));
				}
			}
			requestMap.put(id,  new JsapMetaSparqlRequest(new SparqlRequest(sparql,endPointHost),forceBinds));
		}
		return requestMap;
	}
	
	//-------------------------------------GETTERS and SETTERS
	
	
	public Set<String> getTestNames() {
		// TODO Auto-generated method stub
		return metaTestMap.keySet();
	}

	public String getHost() {
		return _host;
	}


	public int getPort() {
		return _port;
	}


	public String getProtocol() {
		return _protocol;
	}


	public String getOntology() {
		return _ontology;
	}


	public String getGraph() {
		return _graph;
	}


	public MetaTest getTestByName(String name) {
		// TODO Auto-generated method stub
		return metaTestMap.get(name);
	}
	

}
