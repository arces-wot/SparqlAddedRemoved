package edu.lehigh.swat.bench.uba;

import connector.SparqlRequest;
import model.EndPoint;
import model.SparqlObj;
import support.Environment;

public class Ontology {
    
//		private static boolean ontologyNeeded=true;
		
		public static boolean insertOntology() {
			SparqlObj sparql= new SparqlObj(
					"LOAD " +Environment.closeOntology + " INTO GRAPH "+Environment.closeOntology 
					);
			EndPoint endPointHost= new EndPoint(Environment.protocol,Environment.host,Environment.port,"/update");
			
			return new SparqlRequest(sparql,endPointHost).execute().isError();
		}
		
//		public static void insertOntologyIfNeed() {
//				if(ontologyNeeded) {
//					insertOntology();
//					ontologyNeeded=false;
//				}
//		}
//		public static void removeOntology() {
//			System.out.println("removeOntology Not implemented");
//			//ontologyNeeded=true;
//		}
}
