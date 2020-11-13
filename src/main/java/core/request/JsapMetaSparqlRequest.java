package core.request;

import java.util.HashMap;

import addedremoved.epspec.EpSpecFactory;
import addedremoved.epspec.IEndPointSpecification;
import connector.SparqlRequest;
import model.TripleBase;

public class JsapMetaSparqlRequest implements IMetaSparqlRequest {


	public String insertTripleToSparql(String sparql, TripleBase triple, String forceBind, int number) {
		
		boolean needInjeption = sparql.contains(forceBind);		
		String ris = sparql;
		
		
		
		if(needInjeption && triple!=null) {
			triple.reset();
			String triples = "";
			for(int x =0;x<number;x++) {
				triples+=triple.getNextTriple()+". \n";
			}
			ris=ris.replace(forceBind, triples);
		}
		
	
		return ris;
	}
	

	//-----------------------------------------------------------------
	private SparqlRequest req;
	private HashMap<String,TripleBase> forceBinds;


	public JsapMetaSparqlRequest(SparqlRequest req, HashMap<String,TripleBase> forceBinds) {
		super();
		this.req = req;
		this.forceBinds = forceBinds;
	}
	
	
	public SparqlRequest generate(int triples) throws CloneNotSupportedException {
		SparqlRequest reqClone=req.clone();
		String sparql = reqClone.getSparql().getSparqlString();
		for (String key : forceBinds.keySet()) {
			sparql=insertTripleToSparql(sparql,forceBinds.get(key),key,triples);
		}
		reqClone.setSparqlStr(convertVars(sparql));
		return reqClone;
	}
	
	private String convertVars(String sparql) {			
		IEndPointSpecification eps = EpSpecFactory.getInstance();
		return sparql.replace("?s","?"+eps.s()).replace("?p","?"+eps.p()).replace("?o","?"+eps.o()).replace("?g","?"+eps.g());
	}
	
	//---------------------------------------GETTERS and SETTERS
	
	public SparqlRequest getReq() {
		return req;
	}


	public HashMap<String, TripleBase> getForceBinds() {
		return forceBinds;
	}


	
	
}
