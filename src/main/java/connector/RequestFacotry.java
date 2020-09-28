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
		requestMap.put(RequestName.QUERY2.toString(), createQuery2());
		requestMap.put(RequestName.QUERY3.toString(), createQuery3());
		requestMap.put(RequestName.QUERY4.toString(), createQuery4());
		requestMap.put(RequestName.QUERY5.toString(), createQuery5());
		requestMap.put(RequestName.QUERY6.toString(), createQuery6());
		requestMap.put(RequestName.QUERY7.toString(), createQuery7());
		requestMap.put(RequestName.QUERY8.toString(), createQuery8());
		requestMap.put(RequestName.QUERY9.toString(), createQuery9());
		requestMap.put(RequestName.QUERY10.toString(), createQuery10());
		requestMap.put(RequestName.QUERY11.toString(), createQuery11());
		requestMap.put(RequestName.QUERY12.toString(), createQuery12());
		requestMap.put(RequestName.QUERY13.toString(), createQuery13());
		requestMap.put(RequestName.QUERY14.toString(), createQuery14());
		
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
	
	private SparqlRequest createQuery2() {
		/*
			# Query2
			# This query increases in complexity: 3 classes and 3 properties are involved. Additionally, 
			# there is a triangular pattern of relationships between the objects involved.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X, ?Y, ?Z
			WHERE
			{?X rdf:type ub:GraduateStudent .
			  ?Y rdf:type ub:University .
			  ?Z rdf:type ub:Department .
			  ?X ub:memberOf ?Z .
			  ?Z ub:subOrganizationOf ?Y .
			  ?X ub:undergraduateDegreeFrom ?Y}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +" \r\n" + 
				"SELECT ?s  ?p ?o FROM  "+_graph+" WHERE \r\n" + 
				"{?s rdf:type ub:GraduateStudent .\r\n" + 
				"?p rdf:type ub:University .\r\n" + 
				"?o rdf:type ub:Department .\r\n" + 
				"?s ub:memberOf ?o .\r\n" + 
				"?o ub:subOrganizationOf ?p .\r\n" + 
				"?s ub:undergraduateDegreeFrom ?p}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQuery3() {
		/*
			# Query3
			# This query is similar to Query 1 but class Publication has a wide hierarchy.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X
			WHERE
			{?X rdf:type ub:Publication .
		  	?X ub:publicationAuthor 
    		http://www.Department0.University0.edu/AssistantProfessor0}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" + 
				"			SELECT ?X\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"			WHERE\r\n" + 
				"			{?X rdf:type ub:Publication .\r\n" + 
				"		  	?X ub:publicationAuthor \r\n" + 
				"    		<http://www.Department0.University0.edu/AssistantProfessor0>}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQuery4() {
		/*
			# Query4
			# This query has small input and high selectivity. It assumes subClassOf relationship 
			# between Professor and its subclasses. Class Professor has a wide hierarchy. Another 
			# feature is that it queries about multiple properties of a single class.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X, ?Y1, ?Y2, ?Y3
			WHERE
			{?X rdf:type ub:Professor .
			  ?X ub:worksFor <http://www.Department0.University0.edu> .
			  ?X ub:name ?Y1 .
			  ?X ub:emailAddress ?Y2 .
			  ?X ub:telephone ?Y3}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" + 
				"			SELECT ?X ?Y1 ?Y2 ?Y3\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"			WHERE\r\n" + 
				"			{?X rdf:type ub:Professor .\r\n" + 
				"			  ?X ub:worksFor <http://www.Department0.University0.edu> .\r\n" + 
				"			  ?X ub:name ?Y1 .\r\n" + 
				"			  ?X ub:emailAddress ?Y2 .\r\n" + 
				"			  ?X ub:telephone ?Y3}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQuery5() {
		/*
			# Query5
			# This query assumes subClassOf relationship between Person and its subclasses
			# and subPropertyOf relationship between memberOf and its subproperties.
			# Moreover, class Person features a deep and wide hierarchy.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X
			WHERE
			{?X rdf:type ub:Person .
			  ?X ub:memberOf <http://www.Department0.University0.edu>}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" + 
				"			SELECT ?X\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"			WHERE\r\n" + 
				"			{?X rdf:type ub:Person .\r\n" + 
				"			  ?X ub:memberOf <http://www.Department0.University0.edu>}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQuery6() {
		/*
			# Query6
			# This query queries about only one class. But it assumes both the explicit
			# subClassOf relationship between UndergraduateStudent and Student and the
			# implicit one between GraduateStudent and Student. In addition, it has large
			# input and low selectivity.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X WHERE {?X rdf:type ub:Student}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" +  
				"			SELECT ?X " +
				"FROM  "+_graph+"	\r\n" + 
				"WHERE {?X rdf:type ub:Student}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQuery7() {
		/*
			# Query7
			# This query is similar to Query 6 in terms of class Student but it increases in the
			# number of classes and properties and its selectivity is high.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X, ?Y
			WHERE 
			{?X rdf:type ub:Student .
			  ?Y rdf:type ub:Course .
			  ?X ub:takesCourse ?Y .
			  <http://www.Department0.University0.edu/AssociateProfessor0>,   
			  	ub:teacherOf, ?Y}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" +  
				"			SELECT ?X ?Y\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"			WHERE \r\n" + 
				"			{?X rdf:type ub:Student .\r\n" + 
				"			  ?Y rdf:type ub:Course .\r\n" + 
				"			  ?X ub:takesCourse ?Y .\r\n" + 
				"			  <http://www.Department0.University0.edu/AssociateProfessor0>   \r\n" + 
				"			  	ub:teacherOf ?Y}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	
	private SparqlRequest createQuery8() {
		/*
			# Query8
			# This query is further more complex than Query 7 by including one more property.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X, ?Y, ?Z
			WHERE
			{?X rdf:type ub:Student .
			  ?Y rdf:type ub:Department .
			  ?X ub:memberOf ?Y .
			  ?Y ub:subOrganizationOf <http://www.University0.edu> .
			  ?X ub:emailAddress ?Z}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" +
				"			SELECT ?X ?Y ?Z\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"			WHERE\r\n" + 
				"			{?X rdf:type ub:Student .\r\n" + 
				"			  ?Y rdf:type ub:Department .\r\n" + 
				"			  ?X ub:memberOf ?Y .\r\n" + 
				"			  ?Y ub:subOrganizationOf <http://www.University0.edu> .\r\n" + 
				"			  ?X ub:emailAddress ?Z}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQuery9() {
		/*
			# Query9
			# Besides the aforementioned features of class Student and the wide hierarchy of
			# class Faculty, like Query 2, this query is characterized by the most classes and
			# properties in the query set and there is a triangular pattern of relationships.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X, ?Y, ?Z
			WHERE
			{?X rdf:type ub:Student .
			  ?Y rdf:type ub:Faculty .
			  ?Z rdf:type ub:Course .
			  ?X ub:advisor ?Y .
			  ?Y ub:teacherOf ?Z .
			  ?X ub:takesCourse ?Z}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" +
				"			SELECT ?X ?Y ?Z\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"			WHERE\r\n" + 
				"			{?X rdf:type ub:Student .\r\n" + 
				"			  ?Y rdf:type ub:Faculty .\r\n" + 
				"			  ?Z rdf:type ub:Course .\r\n" + 
				"			  ?X ub:advisor ?Y .\r\n" + 
				"			  ?Y ub:teacherOf ?Z .\r\n" + 
				"			  ?X ub:takesCourse ?Z}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQuery10() {
		/*
			# Query10
			# This query differs from Query 6, 7, 8 and 9 in that it only requires the
			# (implicit) subClassOf relationship between GraduateStudent and Student, i.e., 
			#subClassOf rela-tionship between UndergraduateStudent and Student does not add
			# to the results.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X
			WHERE
			{?X rdf:type ub:Student .
			  ?X ub:takesCourse
			<http://www.Department0.University0.edu/GraduateCourse0>}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" +
				"			SELECT ?X\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"			WHERE\r\n" + 
				"			{?X rdf:type ub:Student .\r\n" + 
				"			  ?X ub:takesCourse\r\n" + 
				"			<http://www.Department0.University0.edu/GraduateCourse0>}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQuery11() {
		/*
			# Query11
			# Query 11, 12 and 13 are intended to verify the presence of certain OWL reasoning
			# capabilities in the system. In this query, property subOrganizationOf is defined
			# as transitive. Since in the benchmark data, instances of ResearchGroup are stated
			# as a sub-organization of a Department individual and the later suborganization of 
			# a University individual, inference about the subOrgnizationOf relationship between
			# instances of ResearchGroup and University is required to answer this query. 
			# Additionally, its input is small.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X
			WHERE
			{?X rdf:type ub:ResearchGroup .
			  ?X ub:subOrganizationOf <http://www.University0.edu>}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" +
				"			SELECT ?X\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"			WHERE\r\n" + 
				"			{?X rdf:type ub:ResearchGroup .\r\n" + 
				"			  ?X ub:subOrganizationOf <http://www.University0.edu>}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQuery12() {
		/*
			# Query12
			# The benchmark data do not produce any instances of class Chair. Instead, each
			# Department individual is linked to the chair professor of that department by 
			# property headOf. Hence this query requires realization, i.e., inference that
			# that professor is an instance of class Chair because he or she is the head of a
			# department. Input of this query is small as well.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X, ?Y
			WHERE
			{?X rdf:type ub:Chair .
			  ?Y rdf:type ub:Department .
			  ?X ub:worksFor ?Y .
			  ?Y ub:subOrganizationOf <http://www.University0.edu>}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" +
				"			SELECT ?X ?Y\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"			WHERE\r\n" + 
				"			{?X rdf:type ub:Chair .\r\n" + 
				"			  ?Y rdf:type ub:Department .\r\n" + 
				"			  ?X ub:worksFor ?Y .\r\n" + 
				"			  ?Y ub:subOrganizationOf <http://www.University0.edu>}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}	
	
	private SparqlRequest createQuery13() {
		/*
			# Query13
			# Property hasAlumnus is defined in the benchmark ontology as the inverse of
			# property degreeFrom, which has three subproperties: undergraduateDegreeFrom, 
			# mastersDegreeFrom, and doctoralDegreeFrom. The benchmark data state a person as
			# an alumnus of a university using one of these three subproperties instead of
			# hasAlumnus. Therefore, this query assumes subPropertyOf relationships between 
			# degreeFrom and its subproperties, and also requires inference about inverseOf.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X
			WHERE
			{?X rdf:type ub:Person .
			  <http://www.University0.edu> ub:hasAlumnus ?X}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" +
				"		SELECT ?X\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"		WHERE\r\n" + 
				"		{?X rdf:type ub:Person .\r\n" + 
				"		  <http://www.University0.edu> ub:hasAlumnus ?X}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
	private SparqlRequest createQuery14() {
		/*
			# Query14
			# This query is the simplest in the test set. This query represents those with large input and low selectivity and does not assume any hierarchy information or inference.
			PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
			PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#>
			SELECT ?X
			WHERE {?X rdf:type ub:UndergraduateStudent}
		*/
		SparqlObj sparql= new SparqlObj(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ub: "+ _ontology +"\r\n" +
				"			SELECT ?X\r\n" + 
				"FROM  "+_graph+"	\r\n" + 
				"			WHERE {?X rdf:type ub:UndergraduateStudent}"
				);
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		return new SparqlRequest(sparql,endPointHost);
	}
	
}
