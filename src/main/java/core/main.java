package core;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.jena.base.Sys;

import addedremoved.AddedRemovedGenerator;
import connector.CleanerRDFStore;
import connector.SparqlRequest;
import edu.lehigh.swat.bench.uba.Generator;
import edu.lehigh.swat.bench.uba.Ontology;
import factories.IRequestFactory;
import factories.MetaTestFactory;
import factories.RequestFactory;
import factories.RequestFactory.RequestName;
import factories.MetaRequestFactory;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import it.unibo.arces.wot.sepa.commons.response.Response;
import it.unibo.arces.wot.sepa.commons.response.UpdateResponse;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import model.TestMetric;
import model.TripleBase;
import model.UpdateConstruct;
import support.Environment;
import support.TestBuilder;

public class main {

    private static String graph=Environment.graph;
    private static String ontology = Environment.ontology;	     
    private static boolean ONTOLOGY = false;
    private static boolean POPOLATE = true;
    private static boolean RUN = true;
    private static boolean CLEAN = true;//non rimuove l'ontologia
    
	public static void main (String[] args) {

		 if(ONTOLOGY){
			 loadOntology();
		 }
		 
		 if(CLEAN){
			 cleanStore();
		 }
		 
		 if(POPOLATE){
			 popolateStore();
		 }
		 
		 if(RUN){
			 MetaTestRun();
			 //constructTester();
		 }		 
		
	
	}
	private static void constructTester() {
		ArrayList<TestMetric> phases = new ArrayList<TestMetric> ();
		SparqlRequest deleteUpdate=null;
		SparqlRequest insertUpdate=null;
		SparqlRequest update=(SparqlRequest)MetaRequestFactory.getInstance().getRequestByName("MT3_Update").generate(2);
		UpdateConstruct constructs = AddedRemovedGenerator.getAddedRemovedFrom(update.clone(),phases);
		if(constructs.needDelete()) {
			try {
				deleteUpdate =AddedRemovedGenerator.generateDeleteUpdate(update.clone(),constructs);				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(constructs.needInsert()) {
			try {
				insertUpdate =AddedRemovedGenerator.generateInsertUpdate(update.clone(),constructs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		MT1.setPot(5);
		MT1.setReiteration(2);
		MT1.setMonitor(monitor);
		System.out.println("MT1 Start");
		MT1.execute();
		System.out.println("MT1 End");
	
		 
		MetaTest MT2 = MetaTestFactory.getInstance().getTestByName("DeleteWhere");
		MT2.setPot(4);
		MT2.setReiteration(2);
		MT2.setMonitor(monitor);		
		System.out.println("MT2 Start");
		MT2.execute();
		System.out.println("MT2 End");
	 
		MetaTest MT3 = MetaTestFactory.getInstance().getTestByName("DeleteInsert");
		MT3.setPot(4);
		MT3.setReiteration(2);
		MT3.setMonitor(monitor);		
		System.out.println("MT3 Start");
		MT3.execute();
		System.out.println("MT3 End");
		
		MetaTest MT4 = MetaTestFactory.getInstance().getTestByName("DeleteData");
		MT4.setPot(4);
		MT4.setReiteration(2);
		MT4.setMonitor(monitor);		
		System.out.println("MT4 Start");
		MT4.execute();
		System.out.println("MT4 End");
		
		//close 
		monitor.close();
	}
	
	public static void process(Response res) {
		if(res instanceof QueryResponse ) {
			BindingsResults results = ((QueryResponse) res).getBindingsResults();
			System.out.println("Query Ris: "+ results.toJson().toString());
		}else if(res instanceof UpdateResponse) {
			System.out.println("Update ok: "+ !((UpdateResponse)res).isError());
		}else {
			System.out.println("Res is not valid!");
		}
	}

	
	private static void loadOntology() {
		
		Ontology.insertOntology();
		
	}
	
	private static void popolateStore() {
			
			
			//options:
			//   -univ number of universities to generate; 1 by default
			//   -index starting index of the universities; 0 by default
			//   -seed seed used for random data generation; 0 by default
			//   -daml generate DAML+OIL data; OWL data by default
			//   -onto url of the univ-bench ontology
			  
		    int univNum = 2, startIndex = 0, seed = 0;
		    boolean daml = false;
		    new Generator().start(univNum, startIndex, seed, daml, ontology,graph);
	}
	
	private static void cleanStore() {
		ArrayList<String> graphs = new ArrayList<String>();
		graphs.add(graph);
		System.out.println("Clean success: "+CleanerRDFStore.clean(graphs));
		
		/*
		 * TO check run ---> SELECT ?s ?p ?o FROM <http://lumb/for.sepa.test/workspace/defaultgraph> WHERE { ?s ?p ?o }
		 */
	}
}
