package core.request;

import java.util.HashMap;

import connector.SparqlRequest;
import model.TripleBase;

public class JsapMetaSparqlRequest implements IMetaSparqlRequest {


	public String insertTripleToSparql(String sparql, TripleBase triple, String forceBind, int number) {
		
		boolean needInjeption = sparql.contains(forceBind);		
		String ris = sparql;
		
		
		//La delete DEVE stare prima della insert
		//dato che il numero aggiuntivo per la generazione delle triple non viene resettato
		//e in caso di delete con preparazione, tali triple devono fare mach
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
	
	
	public SparqlRequest generate(int triples) {
		SparqlRequest reqClone=req.clone();
		String sparql = req.getSparql().getSparqlString();
		for (String key : forceBinds.keySet()) {
			sparql=insertTripleToSparql(sparql,forceBinds.get(key),key,triples);
		}
		reqClone.setSparqlStr(sparql);
		return reqClone;
	}
	
	
	//---------------------------------------GETTERS and SETTERS
	
	public SparqlRequest getReq() {
		return req;
	}


	public HashMap<String, TripleBase> getForceBinds() {
		return forceBinds;
	}


	
	
}
