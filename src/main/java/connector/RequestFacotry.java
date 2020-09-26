package connector;

import java.util.HashMap;
import java.util.Set;

import model.EndPoint;
import model.SparqlObj;



public class RequestFacotry implements IRequestFactory{
	public enum RequestName {
		  SIMPLE_INSERT,
		  SIMPLE_QUERY,
		  SIMPLE_DELETE
	}
	private static String _host="localhost";
	private static int _port=8000;
	private static String _protocol="http";
	
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

}
