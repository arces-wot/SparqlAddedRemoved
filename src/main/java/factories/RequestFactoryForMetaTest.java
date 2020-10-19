package factories;

import java.util.HashMap;
import java.util.Set;

import connector.ISparqlRequest;
import connector.SparqlRequest;
import model.EndPoint;
import model.SparqlObj;
import model.TripleBase;
import support.Environment;
import support.TestBuilder;
/*
 * The query QUERY° with ° from 1 to 14
 * refer to http://swat.cse.lehigh.edu/projects/lubm/queries-sparql.txt
 */


public class RequestFactoryForMetaTest implements IRequestFactory{

	
	
	private static RequestFactoryForMetaTest instance=null;
	
	public static RequestFactoryForMetaTest Instance(String protocol,String host, int port) {
	
		instance=new RequestFactoryForMetaTest( protocol, host, port);
		return instance;
	}
	
	public static RequestFactoryForMetaTest getInstance() {
		if(instance==null) {
			instance=new RequestFactoryForMetaTest();
		}
		return instance;
	}
	//--------------------------------------------------
	private String _host=Environment.host;
	private int _port=Environment.port;
	private String _protocol=Environment.protocol;
	private String _ontology=Environment.closeOntology;
	private String _graph=Environment.graph;
	private HashMap<String, ISparqlRequest> requestMap = new HashMap<String, ISparqlRequest>();
	private HashMap<String, TripleBase> tripleBaseMap = new HashMap<String, TripleBase>();
	
	private RequestFactoryForMetaTest(String protocol,String host, int port) {
		this();
		this._host=host;
		this._port=port;
		this._protocol=protocol;
	}
	
	private RequestFactoryForMetaTest() {
		
		//-----------------------------------MetaTest 1
		requestMap.put("MT1_prepare_insert", createInsertMT1());
		requestMap.put("MT1_Query", createQueryMT1());
		requestMap.put("MT1_Update", createInsertMT1());
		requestMap.put("MT1_Rollback", createRollbackMT1());
		requestMap.put("MT1_prepare_rollback", createRollbackMT1());		
		tripleBaseMap.put("MT1", createTripleBaseMT1());
		
		//-----------------------------------MetaTest 2
		//WIP
	}
	

	
	private SparqlRequest createInsertMT1() {
		String sparqlStr = ""+
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+_ontology+"\r\n" + 
				"INSERT DATA  {  GRAPH "+_graph+" { "+
					TestBuilder.getBuilderBindInsert()
				+" } }";
		
		SparqlObj sparql= new SparqlObj(sparqlStr) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createRollbackMT1() {
		String sparqlStr = ""+
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+_ontology+"\r\n" + 
				"DELETE DATA  {  GRAPH "+_graph+" { ?s ub:memberOf <unibo.it> } }";
		
		SparqlObj sparql= new SparqlObj(sparqlStr) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQueryMT1() {
		String sparqlStr = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"			PREFIX ub: "+_ontology+"\r\n" + 
				"			SELECT ?s ub:memberOf <unibo.it> "
				+ "			FROM "+_graph+" WHERE {\r\n" + 
				"			?s ub:memberOf <unibo.it> .\r\n" + 
				"			}";			
		SparqlObj sparql= new SparqlObj(sparqlStr) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private TripleBase createTripleBaseMT1() {

		return  new TripleBase("<Student__X__>","ub:memberOf","<unibo.it>");
	}
	
	//------------------------------------------accessor
	public Set<String> getRequestNames() {
		return requestMap.keySet();
	}

	public ISparqlRequest getRequestByName(String name) {
		return requestMap.get(name);
	}
	
	public Set<String> getTripleBaseNames() {
		return tripleBaseMap.keySet();
	}

	public TripleBase getTripleBaseByName(String name) {
		return tripleBaseMap.get(name);
	}
}
