package addedremoved;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryException;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QueryParseException;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.lang.ParserSPARQL11Update;
import org.apache.jena.sparql.modify.UpdateRequestSink;
import org.apache.jena.sparql.modify.request.UpdateBinaryOp;
import org.apache.jena.sparql.modify.request.UpdateCreate;
import org.apache.jena.sparql.modify.request.UpdateData;
import org.apache.jena.sparql.modify.request.UpdateDeleteWhere;
import org.apache.jena.sparql.modify.request.UpdateDropClear;
import org.apache.jena.sparql.modify.request.UpdateLoad;
import org.apache.jena.sparql.modify.request.UpdateModify;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import connector.RequestFactory;
import connector.SparqlRequest;
import connector.RequestFactory.RequestName;
import model.UpdateConstruct;

public class GraphAnalyzer {

	protected static String arqDefaultGraphNodeUri = "urn:x-arq:DefaultGraphNode";
	
	public static String getGraph(String sparql) {
		String lower = sparql.toLowerCase();
		if(lower.contains("graph")){
			int start = lower.indexOf("graph")+5;
			String temp = sparql.substring(start);
			return temp.substring(0,temp.indexOf(">")+1);
		}
		if(lower.contains("with ")){
			int start = lower.indexOf("with")+4;
			String temp = sparql.substring(start);
			return temp.substring(0,temp.indexOf(">")+1);
		}
		return null;
	}
	
	public static void testGraphOf(String sparql) {
		SPARQLAnalyzer sa = new SPARQLAnalyzer(sparql);
		UpdateConstruct constructs = sa.getConstruct();
	}
	
	 
		public static void main (String[] args) {

			String test1 ="PREFIX ub: <http://lumb/for.sepa.test/ontology#>\r\n" + 
					"			INSERT \r\n" + 
					"			{ GRAPH <http://lumb/for.sepa.test/workspace/defaultgraph>{\r\n" + 
					"			 <http://www.Department2.University0.edu/GraduateStudent0>\r\n" + 
					"			 ub:undergraduateDegreeFrom \r\n" + 
					"			?Y\r\n" + 
					"			} }\r\n" + 
					"			WHERE {GRAPH <http://lumb/for.sepa.test/workspace/defaultgraph>{\r\n" + 
					"			<http://www.Department2.University0.edu/GraduateStudent0> rdf:type ub:GraduateStudent .\r\n" + 
					"			?Y rdf:type ub:University .\r\n" + 
					"			?Z rdf:type ub:Department .\r\n" + 
					"			<http://www.Department2.University0.edu/GraduateStudent0> ub:memberOf ?Z .\r\n" + 
					"			?Z ub:subOrganizationOf ?Y .\r\n" + 
					"			}}";
			
			String test2 ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
					"PREFIX ub: <http://lumb/for.sepa.test/ontology#>\r\n" + 
					"WITH <http://lumb/for.sepa.test/workspace/defaultgraph>\r\n" + 
					"DELETE {\r\n" + 
					" ?X ub:publicationAuthor <http://www.Department0.University0.edu/AssistantProfessor0>\r\n" + 
					"}\r\n" + 
					"INSERT { \r\n" + 
					"\r\n" + 
					"<http://www.department0.university0.edu/AssistantProfessor0/PubTest1>  rdf:type ub:Publication .\r\n" + 
					"<http://www.department0.university0.edu/AssistantProfessor0/PubTest2>  rdf:type ub:Publication .\r\n" + 
					"\r\n" + 
					"<http://www.department0.university0.edu/AssistantProfessor0/PubTest1> ub:publicationAuthor <http://www.Department0.University0.edu/AssistantProfessor0>.\r\n" + 
					"<http://www.department0.university0.edu/AssistantProfessor0/PubTest2> ub:publicationAuthor <http://www.Department0.University0.edu/AssistantProfessor0>.\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"}\r\n" + 
					"WHERE\r\n" + 
					"{\r\n" + 
					"?X rdf:type ub:Publication .\r\n" + 
					"?X ub:publicationAuthor <http://www.Department0.University0.edu/AssistantProfessor0>\r\n" + 
					"}\r\n" + 
					"";
			
		//	System.out.println(getGraph(test2));
			String sparql = ((SparqlRequest)RequestFactory.getInstance().getRequestByName(RequestName.UPDATE_FOR_Q3.toString())).getSparql().getSparqlString();
			System.out.println(getGraphURIs(sparql).iterator().next());
			testGraphOf(sparql);
		
		}
		
		
//codice ottenuto dal Sepa engine, it.unibo.arces.wot.sepa.engine.scheduling; InternalUpdateRequest
		public static Set<String> getGraphURIs(String sparql) throws QueryParseException {		
			Set<String> rdfDataSet = new HashSet<String>();
			UpdateRequest upd = new UpdateRequest();
			UpdateRequestSink sink = new UpdateRequestSink(upd);

			new ParserSPARQL11Update().parse(sink, sparql);

			for (Update op : upd.getOperations()) {
				if (op instanceof UpdateModify) {
					UpdateModify tmp = (UpdateModify) op;
					
					// WITH
					Node node = tmp.getWithIRI();
					if (node != null)
						if (node.isURI()) {
							rdfDataSet.add(node.getURI());
						}
					
					// USING
					for (Node n : tmp.getUsing()) {
						if (n.isURI())
							rdfDataSet.add(n.getURI());
						else if (n.isVariable())
							//TODO: check
							rdfDataSet.add("*");
					}
					
					// USING NAMED
					for (Node n : tmp.getUsingNamed()) {
						if (n.isURI())
							rdfDataSet.add(n.getURI());
						else if (n.isVariable())
							//TODO: check
							rdfDataSet.add("*");
					}
					
					// QUADS
					for (Quad q : tmp.getInsertQuads()) {
						Node n = q.getGraph();
						if (n.isURI())
							if (!n.getURI().equals(arqDefaultGraphNodeUri)) rdfDataSet.add(n.getURI());
						else if (n.isVariable())
							//TODO: check
							rdfDataSet.add("*");
					}
					for (Quad q : tmp.getDeleteQuads()) {
						Node n = q.getGraph();
						if (n.isURI())
							if (!n.getURI().equals(arqDefaultGraphNodeUri)) rdfDataSet.add(n.getURI());
						else if (n.isVariable())
							//TODO: check
							rdfDataSet.add("*");
					}
				} else if (op instanceof UpdateBinaryOp) {
					UpdateBinaryOp tmp = (UpdateBinaryOp) op;
					
					Node node = tmp.getDest().getGraph();
					
					// ADD, COPY, MOVE
					if (node.isURI())
						rdfDataSet.add(node.getURI());
					else if (node.isVariable())
						//TODO: check
						rdfDataSet.add("*");
				} else if (op instanceof UpdateCreate) {
					UpdateCreate tmp = (UpdateCreate) op;
					
					Node node = tmp.getGraph();
					
					// CREATE
					if (node.isURI())
						rdfDataSet.add(node.getURI());
					else if (node.isVariable())
						//TODO: check
						rdfDataSet.add("*");
				} else if (op instanceof UpdateData) {
					UpdateData tmp = (UpdateData) op;
					
					// UPDATE DATA
					for (Quad q : tmp.getQuads()) {
						Node node = q.getGraph();
						if (node.isURI())
							rdfDataSet.add(node.getURI());
						else if (node.isVariable())
							//TODO: check
							rdfDataSet.add("*");
					}
				} else if (op instanceof UpdateDeleteWhere) {
					UpdateDeleteWhere tmp = (UpdateDeleteWhere) op;
					
					// UPDATE DELETE WHERE
					for (Quad q : tmp.getQuads()) {
						Node node = q.getGraph();
						if (node.isURI())
							rdfDataSet.add(node.getURI());
						else if (node.isVariable())
							//TODO: check
							rdfDataSet.add("*");
					}
				} else if (op instanceof UpdateDropClear) {
					UpdateDropClear tmp = (UpdateDropClear) op;
					
					Node node = tmp.getGraph();
					
					// DROP, CLEAR
					if (node.isURI())
						rdfDataSet.add(node.getURI());
					else if (node.isVariable())
						//TODO: check
						rdfDataSet.add("*");
				} else if (op instanceof UpdateLoad) {
					UpdateLoad tmp = (UpdateLoad) op;
					
					Node node = tmp.getDest();
					
					// LOAD
					if (node.isURI())
						rdfDataSet.add(node.getURI());
					else if (node.isVariable())
						//TODO: check
						rdfDataSet.add("*");
				} 
			}

			return rdfDataSet;
		}
}
