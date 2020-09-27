package connector;

import java.util.HashMap;
import java.util.Set;

import model.EndPoint;
import model.SparqlObj;
/*
 * The query QUERY° with ° from 1 to 14
 * refer to http://swat.cse.lehigh.edu/projects/lubm/queries-sparql.txt
 */


public class RequestFacotry implements IRequestFactory{
	public enum RequestName {
		  SIMPLE_INSERT,
		  SIMPLE_QUERY,
		  SIMPLE_DELETE,
		  QUERY1,
		  QUERY2,
		  QUERY3,
		  QUERY4,
		  QUERY5,
		  QUERY6,
		  QUERY7,
		  QUERY8,
		  QUERY9,
		  QUERY10,
		  QUERY11,
		  QUERY12,
		  QUERY13,
		  QUERY14,
	}
	private static String _host="localhost";
	private static int _port=8000;
	private static String _protocol="http";
	private static String _ontology ="<http://lumb/for.sepa.test/ontology#>";
	private static String _graph ="<http://lumb/for.sepa.test/workspace/defaultgraph>";
	
	private static RequestFacotry instance=null;
	
	public static RequestFacotry Instance(String protocol,String host, int port) {
		_host=host;
		_port=port;
		_protocol=protocol;
		instance=new RequestFacotry();
		return instance;
	}
	
	public static RequestFacotry getIntance() {
		if(instance==null) {
			instance=new RequestFacotry();
		}
		return instance;
	}
	//--------------------------------------------------
	
	private HashMap<String, ISparqlRequest> requestMap = new HashMap<String, ISparqlRequest>();
	private RequestFacotry() {
		
		
		requestMap.put(RequestName.SIMPLE_INSERT.toString(), createSimpleInsert());
		requestMap.put(RequestName.SIMPLE_QUERY.toString(), createSimpleQuery());
		requestMap.put(RequestName.SIMPLE_DELETE.toString(), createSimpleDelete());
		requestMap.put(RequestName.QUERY1.toString(), createQuery1());
		
	}
	
	public Set<String> getRequestNames() {
		return requestMap.keySet();
	}

	public ISparqlRequest getRequestByName(String name) {
		return requestMap.get(name);
	}
	
	private SparqlRequest createSimpleInsert() {
		SparqlObj sparql= new SparqlObj("INSERT DATA  {  GRAPH <urn:sparql:tests:insert:data> { <#book1> <#price> 42 } }") ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createSimpleQuery() {
		SparqlObj sparql= new SparqlObj("SELECT * FROM <urn:sparql:tests:insert:data> WHERE { ?s ?p ?o }") ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createSimpleDelete() {
		SparqlObj sparql= new SparqlObj("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>WITH <urn:sparql:tests:insert:data>DELETE{?book ?p ?v}WHERE{ ?book ?p ?v;?p 42} ") ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/update");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQuery1() {
		/*
			# Query1
			# This query bears large input and high selectivity. It queries about just one class and
			# one property and does not assume any hierarchy information or inference.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X	
			WHERE
			{?X rdf:type ub:GraduateStudent .
			  ?X ub:takesCourse
			http://www.Department0.University0.edu/GraduateCourse0}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" + 
				"SELECT ?X	\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"WHERE\r\n" + 
				"{?X rdf:type ub:GraduateStudent .\r\n" + 
				"?X ub:takesCourse\r\n" + 
				"<http://www.Department0.University0.edu/GraduateCourse0>}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
}
