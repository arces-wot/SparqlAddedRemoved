package addedremoved;

import connector.RequestFactory;
import connector.SparqlRequest;
import connector.RequestFactory.RequestName;
import model.UpdateConstruct;

public class GraphAnalyzer {

	
	public static String getGraph(String sparql) {
		String lower = sparql.toLowerCase();
		if(lower.contains("graph")){
			int start = lower.indexOf("graph")+5;
			String temp = sparql.substring(start);
			return temp.substring(0,temp.indexOf(">")+1);
		}
		if(lower.contains("with")){
			int start = lower.indexOf("with")+4;
			String temp = sparql.substring(start);
			return temp.substring(0,temp.indexOf(">")+1);
		}
		return null;
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
			
			System.out.println(getGraph(test1));
			
		
		}
}
