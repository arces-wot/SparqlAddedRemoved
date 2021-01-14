package core;

import java.io.IOException;
import java.util.ArrayList;
import connector.CleanerRDFStore;
import connector.SparqlRequest;
import core.test.ITestVisitor;
import core.test.MetaTest;
import core.test.TestVisitorOutputJsonFile;
import edu.lehigh.swat.bench.uba.Generator;
import edu.lehigh.swat.bench.uba.Ontology;
import factories.JsapMetaTestFactory;
import factories.MetaTestFactory;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import model.EndPoint;
import model.SparqlObj;
import support.Environment;

public class main {

    private static String graph=Environment.graph;
    private static String ontology = Environment.ontology;	     
    private static boolean ONTOLOGY = false;
    private static boolean POPOLATE = false;
    private static boolean RUN = false;
    private static boolean CLEAN = true;//non rimuove l'ontologia
    private static int graphsNumber =8;
    
	public static void main (String[] args) {

		 if(ONTOLOGY){
			 loadOntology();
		 }
		 
		 if(CLEAN){
			 cleanStore(graphsNumber);
		 }
		 
		 if(POPOLATE){
			 popolateStore(graphsNumber);
		 }
		 
		 if(RUN){
			 //MetaTestRun();
			 //constructTester();
			 testQuerySubscribe();
			 //jsapIntegration();
		 }		 
		
	
	}

	private static void testQuerySubscribe() {
		 String _host=Environment.host;
		 int _port=Environment.port;
		 String _protocol=Environment.protocol;
		SparqlObj sparql= new SparqlObj(
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" + 
				"PREFIX dc: <http://purl.org/dc/elements/1.1/>\r\n"+
				"SELECT ?who ?g ?mbox\r\n" + 
				"FROM <prova2>\r\n" + 
				"FROM NAMED <http://example.org/alice>\r\n" + 
				"FROM NAMED <http://example.org/bob>\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"   ?g dc:publisher ?who .\r\n" + 
				"   GRAPH ?g { ?x foaf:mbox ?mbox }\r\n" + 
				"}"
				) ;
		EndPoint endPointHost= new EndPoint(_protocol,_host,_port,"/query");
		QueryResponse queryR= ((QueryResponse)( new SparqlRequest(sparql,endPointHost).execute()));
		System.out.println("->"+queryR.getBindingsResults().toJson().toString());
		
	}
	
	private static void jsapIntegration() {
		ITestVisitor monitor=null;
		try {				
			monitor = new TestVisitorOutputJsonFile("E:\\prova.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		MetaTest MT1 = JsapMetaTestFactory.getInstance().getTestByName("INSERT_DATA");
		MT1.setPot(4);
		MT1.setReiteration(5);
		MT1.setMonitor(monitor);
		System.out.println("MT1 Start");
		MT1.execute();
		System.out.println("MT1 End");

		//close 
		monitor.close();
	}
	
	private static void constructTester() {
		 
		MetaTest MT1 = JsapMetaTestFactory.getInstance().getTestByName("UpdateModify2");
		MT1.setPot(1);
		MT1.setReiteration(1);
		System.out.println("MT Start");
		MT1.execute();
		System.out.println("MT End");

	}
	
	private static void MetaTestRun() {
		ITestVisitor monitor=null;
		try {				
			monitor = new TestVisitorOutputJsonFile("E:\\prova.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		MetaTest MT1 = MetaTestFactory.getInstance().getTestByName("InsertData");	
		MT1.setPot(4);
		MT1.setReiteration(5);
		MT1.setMonitor(monitor);
		System.out.println("MT1 Start");
		MT1.execute();
		System.out.println("MT1 End");
	
		 
		MetaTest MT2 = MetaTestFactory.getInstance().getTestByName("DeleteWhere");
		MT2.setPot(4);
		MT2.setReiteration(5);
		MT2.setMonitor(monitor);		
		System.out.println("MT2 Start");
		MT2.execute();
		System.out.println("MT2 End");
	 
		MetaTest MT3 = MetaTestFactory.getInstance().getTestByName("DeleteInsert");
		MT3.setPot(4);
		MT3.setReiteration(5);
		MT3.setMonitor(monitor);		
		System.out.println("MT3 Start");
		MT3.execute();
		System.out.println("MT3 End");
		
		MetaTest MT4 = MetaTestFactory.getInstance().getTestByName("DeleteData");
		MT4.setPot(4);
		MT4.setReiteration(5);
		MT4.setMonitor(monitor);		
		System.out.println("MT4 Start");
		MT4.execute();
		System.out.println("MT4 End");
		
		//close 
		monitor.close();
	}
	

	
	private static void loadOntology() {
		
		System.out.println("LoadOntology error: "+ Ontology.insertOntology());
		
	}
	
	private static void popolateStore(int graphNumber) {
			
			
			//options:
			//   -univ number of universities to generate; 1 by default
			//   -index starting index of the universities; 0 by default
			//   -seed seed used for random data generation; 0 by default
			//   -daml generate DAML+OIL data; OWL data by default
			//   -onto url of the univ-bench ontology
			  
		    int univNum = 1, startIndex = 0, seed = 0;
		    boolean daml = false;
		    
			for(int x=0;x<graphNumber ;x++) {
				new Generator().start(univNum, startIndex, seed, daml, ontology,Environment.generateGraphN(graphNumber));
				System.out.println("Graphs: " +(x+1)+ "/"+graphNumber);
			}
		  
	}
	
	private static void cleanStore(int graphNumber) {
		ArrayList<String> graphs = new ArrayList<String>();	
		for(int x=0;x<graphNumber ;x++) {
			graphs.add(Environment.generateGraphN(graphNumber));
		}
		System.out.println("Clean success: "+CleanerRDFStore.clean(graphs));
		/*
		 * TO check run ---> SELECT ?s ?p ?o FROM <http://lumb/for.sepa.test/workspace/defaultgraph> WHERE { ?s ?p ?o }
		 */
	}
}
