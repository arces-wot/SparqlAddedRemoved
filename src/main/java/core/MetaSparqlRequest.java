package core;

import connector.SparqlRequest;
import model.TripleBase;

public class MetaSparqlRequest implements IMetaSparqlRequest {
	
	//-----------------------------------------------------STATIC PART
	private static String builderBindInsert = "_?_I_TRIPLES_?_";
	private static String builderBindDelete = "_?_D_TRIPLES_?_";
	
	public static String getBuilderBindInsert() {
		return builderBindInsert;
	}
	
	public static String getBuilderBindDelete() {
		return builderBindDelete;
	}
	
	public static String insertTripleToSparql(String sparql, TripleBase tripleInsert,TripleBase tripleDelete, int number) {
		
		boolean needInsertInjeption = sparql.contains(builderBindInsert);
		boolean needDeleteInjeption = sparql.contains(builderBindDelete);		
		String ris = sparql;
		
		
		//La delete DEVE stare prima della insert
		//dato che il numero aggiuntivo per la generazione delle triple non viene resettato
		//e in caso di delete con preparazione, tali triple devono fare mach
		if(needDeleteInjeption && tripleDelete!=null) {
			tripleDelete.reset();
			String triples = "";
			for(int x =0;x<number;x++) {
				triples+=tripleDelete.getNextTriple()+". \n";
			}
			ris=ris.replace(builderBindDelete, triples);
		}
		
		
		if(needInsertInjeption && tripleInsert!=null) {
			tripleInsert.reset();
			String triples = "";
			for(int x =0;x<number;x++) {
				triples+=tripleInsert.getNextTriple()+". \n";
			}
			ris=ris.replace(builderBindInsert, triples);
		}
		return ris;
	}
	

	//-----------------------------------------------------------------
	private SparqlRequest req;
	private TripleBase tripleInsert=null;
	private TripleBase tripleDelete=null;
	
	public MetaSparqlRequest(SparqlRequest req) {
		super();
		this.req = req;
	}

	public MetaSparqlRequest(SparqlRequest req, TripleBase tripleInsert, TripleBase tripleDelete) {
		super();
		this.req = req;
		this.tripleInsert = tripleInsert;
		this.tripleDelete = tripleDelete;
	}
	
	
	public SparqlRequest generate(int triples) {
		SparqlRequest reqClone=req.clone();
		reqClone.setSparqlStr(insertTripleToSparql(req.getSparql().getSparqlString(),tripleInsert,tripleDelete,triples));
		return reqClone;
	}
	
	
	//---------------------------------------GETTERS and SETTERS
	
	public SparqlRequest getReq() {
		return req;
	}

	
	public void setTripleInsert(TripleBase tripleInsert) {
		this.tripleInsert = tripleInsert;
	}

	public void setTripleDelete(TripleBase tripleDelete) {
		this.tripleDelete = tripleDelete;
	}

	public TripleBase getTripleInsert() {
		return tripleInsert;
	}

	public TripleBase getTripleDelete() {
		return tripleDelete;
	}

	
}
