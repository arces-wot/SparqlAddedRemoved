package factories;

import java.util.HashMap;
import java.util.Set;

import connector.ISparqlRequest;
import connector.SparqlRequest;
import core.request.IMetaSparqlRequest;
import core.request.MetaSparqlRequest;
import model.EndPoint;
import model.SparqlObj;
import model.TripleBase;
import support.Environment;
import support.TestBuilder;
/*
 * The query QUERY� with � from 1 to 14
 * refer to http://swat.cse.lehigh.edu/projects/lubm/queries-sparql.txt
 */


public class MetaRequestFactory implements IMetaRequestFactory{

	private static String builderBindInsert = "_?_I_TRIPLES_?_";
	private static String builderBindDelete = "_?_D_TRIPLES_?_";
	
	
	private static MetaRequestFactory instance=null;
	
	public static MetaRequestFactory Instance(String protocol,String host, int port) {
	
		instance=new MetaRequestFactory( protocol, host, port);
		return instance;
	}
	
	public static MetaRequestFactory getInstance() {
		if(instance==null) {
			instance=new MetaRequestFactory();
		}
		return instance;
	}
	//--------------------------------------------------
	private String _host=Environment.host;
	private int _port=Environment.port;
	private String _protocol=Environment.protocol;
	private String _ontology=Environment.closeOntology;
	private String _graph=Environment.graph;
	private HashMap<String, IMetaSparqlRequest> requestMap = new HashMap<String, IMetaSparqlRequest>();
	
	private MetaRequestFactory(String protocol,String host, int port) {
		this();
		this._host=host;
		this._port=port;
		this._protocol=protocol;
	}
	
	private MetaRequestFactory() {
		
		//-----------------------------------MetaTest 1 (ONLY INSERT DATA)
		requestMap.put("MT1_prepare_insert", createInsertMT1());
		requestMap.put("MT1_Query", createQueryMT1());
		requestMap.put("MT1_Update", createUpdateMT1());
		requestMap.put("MT1_Rollback", createRollbackMT1());
		requestMap.put("MT1_prepare_rollback", createRollbackMT1());	
		
		//-----------------------------------MetaTest 2 (ONLY DELETE WHERE)
		requestMap.put("MT2_prepare_insert", createInsertMT2());
		requestMap.put("MT2_Query", createQueryMT2());
		requestMap.put("MT2_Update", createUpdateMT2());
		requestMap.put("MT2_Rollback", createRollbackMT2());
		requestMap.put("MT2_prepare_rollback", createRollbackPreInsered());	
		
		//-----------------------------------MetaTest 3 (INSERT DATA + DELETE WHERE)
		requestMap.put("MT3_prepare_insert", createPrepareInsertMT3());
		requestMap.put("MT3_Query", createQueryMT3());
		requestMap.put("MT3_Update", createUpdateMT3());
		requestMap.put("MT3_Rollback", createRollbackMT3());
		requestMap.put("MT3_prepare_rollback", createRollbackPrepareMT3());	
		
		//-----------------------------------MetaTest 4 (DELETE DATA)
		requestMap.put("MT4_prepare_insert", createPrepareMT4());
		requestMap.put("MT4_Query", createQueryMT4());
		requestMap.put("MT4_Update", createUpdateMT4());
		requestMap.put("MT4_Rollback", createRollbackMT4());
		requestMap.put("MT4_prepare_rollback", createPrepareRollbackMT4());	
	}
	
//-------------------------------------------------------------------------------MT4 START
	
		private MetaSparqlRequest createPrepareMT4() {
			String sparqlStr = ""+
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
					"PREFIX ub: "+_ontology+"\r\n" + 
					"INSERT DATA  {  GRAPH "+_graph+" { "+
					builderBindInsert
					+" } }";
			
			SparqlObj sparql= new SparqlObj(sparqlStr) ;
			EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
			MetaSparqlRequest msr = new MetaSparqlRequest(new SparqlRequest(sparql,endPointHost));
			msr.setTripleInsert(createTripleBaseMT1());
			return msr;
		}
		

		private MetaSparqlRequest createUpdateMT4() {
			String sparqlStr = ""+
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
					"PREFIX ub: "+_ontology+"\r\n" + 
					"DELETE DATA { GRAPH "+_graph+" { "+
					builderBindDelete
							+ " } }";			
			SparqlObj sparql= new SparqlObj(sparqlStr) ;
			EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
			MetaSparqlRequest msr = new MetaSparqlRequest(new SparqlRequest(sparql,endPointHost));
			msr.setTripleDelete(createTripleBaseMT1());
			return msr;
		}
		
		
		private MetaSparqlRequest createRollbackMT4() {
			String sparqlStr = ""+
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
					"PREFIX ub: "+_ontology+"\r\n" + 
					"INSERT DATA  {  GRAPH "+_graph+" { "+
					builderBindInsert
					+" } }";
			
			SparqlObj sparql= new SparqlObj(sparqlStr) ;
			EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
			MetaSparqlRequest msr = new MetaSparqlRequest(new SparqlRequest(sparql,endPointHost));
			msr.setTripleInsert(createTripleBaseMT1());
			return msr;
		}
		
		private MetaSparqlRequest createQueryMT4() {
			return createQueryMT1();
		}
		
		private MetaSparqlRequest createPrepareRollbackMT4() {
			return createRollbackMT1();
		}
		

//-------------------------------------------------------------------------------MT4 END
//-------------------------------------------------------------------------------MT3 START
	
		private MetaSparqlRequest createPrepareInsertMT3() {
			String sparqlStr = ""+
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
					"PREFIX ub: "+_ontology+"\r\n" + 
					"INSERT DATA  {  GRAPH "+_graph+" { "+
					builderBindInsert
					+" } }";
			
			SparqlObj sparql= new SparqlObj(sparqlStr) ;
			EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
			MetaSparqlRequest msr = new MetaSparqlRequest(new SparqlRequest(sparql,endPointHost));
			msr.setTripleInsert(createTripleBaseMT3A());
			return msr;
		}
		private MetaSparqlRequest createQueryMT3() {
			//same query of MT1
			return createQueryMT1();
		}
		private MetaSparqlRequest createUpdateMT3() {
			String sparqlStr = ""+
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
					"PREFIX ub: "+_ontology+"\r\n" + 
					"DELETE {  GRAPH "+_graph+" { "+
					builderBindDelete
					+" } } INSERT {  GRAPH "+_graph+"{"+
					builderBindInsert
					+ " } } WHERE {?s ?p ?o}";			
			SparqlObj sparql= new SparqlObj(sparqlStr) ;
			EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
			MetaSparqlRequest msr = new MetaSparqlRequest(new SparqlRequest(sparql,endPointHost));
			msr.setTripleInsert(createTripleBaseMT3B());
			msr.setTripleDelete(createTripleBaseMT3A());
			return msr;
		}
		
		private MetaSparqlRequest createRollbackMT3() {
			//is the same of the update, with inverted triples
			String sparqlStr = ""+
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
					"PREFIX ub: "+_ontology+"\r\n" + 
					"DELETE {  GRAPH "+_graph+" { "+
					builderBindDelete
					+" } }INSERT {  GRAPH "+_graph+"{"+
					builderBindInsert
					+ " } } WHERE {?s ?p ?o}";	
			SparqlObj sparql= new SparqlObj(sparqlStr) ;
			EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
			MetaSparqlRequest msr = new MetaSparqlRequest(new SparqlRequest(sparql,endPointHost));
			msr.setTripleInsert(createTripleBaseMT3A());
			msr.setTripleDelete(createTripleBaseMT3B());
			return msr;
		}
		private MetaSparqlRequest createRollbackPrepareMT3() {
			return createRollbackMT1();
		}
		private TripleBase createTripleBaseMT3A() {
			return  new TripleBase("<http://www.unibo.it/Student_A__X__>","ub:memberOf","<http://www.unibo.it>");
		}
		private TripleBase createTripleBaseMT3B() {
			return  new TripleBase("<http://www.unibo.it/Student_B__X__>","ub:memberOf","<http://www.unibo.it>");
		}
//-------------------------------------------------------------------------------MT3 END	
//-------------------------------------------------------------------------------MT2 START
	
	private MetaSparqlRequest createInsertMT2() {
		//is the same of MT1
		return createInsertMT1();
	}
	private MetaSparqlRequest createQueryMT2() {
		//is the same of MT1
		return createQueryMT1();
	}
	private MetaSparqlRequest createUpdateMT2() {
		//inverse of MT1
		return createRollbackMT1();
	}
	
	private MetaSparqlRequest createRollbackMT2() {
		//inverse of MT1
		return createUpdateMT1();
	}
	private MetaSparqlRequest createRollbackPreInsered() {
		//is the same of the rollback of MT1
		return createRollbackMT1();
	}
//-------------------------------------------------------------------------------MT2 END	
//-------------------------------------------------------------------------------MT1 START
	
	private MetaSparqlRequest createInsertMT1() {
		String sparqlStr = ""+
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+_ontology+"\r\n" + 
				"INSERT DATA  {  GRAPH "+_graph+" { "+
				builderBindInsert
				+" } }";
		
		SparqlObj sparql= new SparqlObj(sparqlStr) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
		MetaSparqlRequest msr = new MetaSparqlRequest(new SparqlRequest(sparql,endPointHost));
		msr.setTripleInsert(createTripleBaseMT1());
		return msr;
	}
	
	private MetaSparqlRequest createUpdateMT1() {
		return createInsertMT1();
	}
	
	private MetaSparqlRequest createRollbackMT1() {
		String sparqlStr = ""+
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+_ontology+"\r\n" + 
				"DELETE WHERE { GRAPH "+_graph+" { ?s ub:memberOf <http://www.unibo.it> } }";
		
		SparqlObj sparql= new SparqlObj(sparqlStr) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
		return new MetaSparqlRequest(new SparqlRequest(sparql,endPointHost));
	}
	
	private MetaSparqlRequest createQueryMT1() {
		String sparqlStr = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"			PREFIX ub: "+_ontology+"\r\n" + 
				"			SELECT ?s ?p ?o"
				+ "			FROM "+_graph+" WHERE {\r\n" + 
				"			?s ?p ?o .\r\n" + 
				"			?s ub:memberOf <http://www.unibo.it> .\r\n"+
				"			}";			
		SparqlObj sparql= new SparqlObj(sparqlStr) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");		
		return new MetaSparqlRequest(new SparqlRequest(sparql,endPointHost));
	}
	
	private TripleBase createTripleBaseMT1() {
		return  new TripleBase("<http://www.unibo.it/Student__X__>","ub:memberOf","<http://www.unibo.it>");
	}

//-------------------------------------------------------------------------------MT1 END
	//------------------------------------------accessor
	public Set<String> getRequestNames() {
		return requestMap.keySet();
	}

	public IMetaSparqlRequest getRequestByName(String name) {
		return requestMap.get(name);
	}
	
	
}
