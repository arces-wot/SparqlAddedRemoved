package connector;

import java.util.HashMap;
import java.util.Set;

import model.EndPoint;
import model.SparqlObj;
/*
 * The query QUERY° with ° from 1 to 14
 * refer to http://swat.cse.lehigh.edu/projects/lubm/queries-sparql.txt
 */


public class RequestFactoryTemp implements IRequestFactory{
	
	private static String _host="localhost";
	private static int _port=8000;
	private static String _protocol="http";
	private static String _ontology ="<http://lumb/for.sepa.test/ontology#>";
	private static String _graph ="<http://lumb/for.sepa.test/workspace/defaultgraph>";
	
	private static RequestFactoryTemp instance=null;
	
	public static RequestFactoryTemp Instance(String protocol,String host, int port) {
		_host=host;
		_port=port;
		_protocol=protocol;
		instance=new RequestFactoryTemp();
		return instance;
	}
	
	public static RequestFactoryTemp getInstance() {
		if(instance==null) {
			instance=new RequestFactoryTemp();
		}
		return instance;
	}
	//--------------------------------------------------
	
	private HashMap<String, ISparqlRequest> requestMap = new HashMap<String, ISparqlRequest>();
	private RequestFactoryTemp() {
		
		
		requestMap.put("CONSTRUCT_1", createConstruct1());
		requestMap.put("CONSTRUCT_2", createConstruct2());
	}
	
	public Set<String> getRequestNames() {
		return requestMap.keySet();
	}

	public ISparqlRequest getRequestByName(String name) {
		return requestMap.get(name);
	}
	
	private SparqlRequest createConstruct1() {
		SparqlObj sparql= new SparqlObj("CONSTRUCT \r\n" + 
				"  { \r\n" + 
				"    GRAPH <http://lumb/for.sepa.test/workspace/defaultgraph> \r\n" + 
				"      { <http://www.Department2.University0.edu/GraduateStudent0> <http://lumb/for.sepa.test/ontology#undergraduateDegreeFrom> ?Y .}\r\n" + 
				"  }\r\n" + 
				"WHERE\r\n" + 
				"  { <http://www.Department2.University0.edu/GraduateStudent0>\r\n" + 
				"              a                     <http://lumb/for.sepa.test/ontology#GraduateStudent> .\r\n" + 
				"    ?Y        a                     <http://lumb/for.sepa.test/ontology#University> .\r\n" + 
				"    ?Z        a                     <http://lumb/for.sepa.test/ontology#Department> .\r\n" + 
				"    <http://www.Department2.University0.edu/GraduateStudent0>\r\n" + 
				"              <http://lumb/for.sepa.test/ontology#memberOf>  ?Z .\r\n" + 
				"    ?Z        <http://lumb/for.sepa.test/ontology#subOrganizationOf>  ?Y\r\n" + 
				"  }") ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	
	private SparqlRequest createConstruct2() {
		SparqlObj sparql= new SparqlObj("CONSTRUCT \r\n" + 
				"  { \r\n" + 
				"    <http://www.Department2.University0.edu/GraduateStudent0> <http://lumb/for.sepa.test/ontology#undergraduateDegreeFrom> ?Y .}\r\n" + 
				"WHERE\r\n" + 
				"  { <http://www.Department2.University0.edu/GraduateStudent0>\r\n" + 
				"              a                     <http://lumb/for.sepa.test/ontology#GraduateStudent> .\r\n" + 
				"    ?Y        a                     <http://lumb/for.sepa.test/ontology#University> .\r\n" + 
				"    ?Z        a                     <http://lumb/for.sepa.test/ontology#Department> .\r\n" + 
				"    <http://www.Department2.University0.edu/GraduateStudent0>\r\n" + 
				"              <http://lumb/for.sepa.test/ontology#memberOf>  ?Z .\r\n" + 
				"    ?Z        <http://lumb/for.sepa.test/ontology#subOrganizationOf>  ?Y\r\n" + 
				"  }") ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	

	
	
}
