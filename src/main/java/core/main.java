package core;
import java.util.ArrayList;

import addedremoved.AddedRemovedGenerator;
import connector.CleanerRDFStore;
import connector.RequestFactory;
import connector.SparqlRequest;
import connector.RequestFactory.RequestName;
import edu.lehigh.swat.bench.uba.Generator;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import it.unibo.arces.wot.sepa.commons.response.Response;
import it.unibo.arces.wot.sepa.commons.response.UpdateResponse;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import model.UpdateConstruct;

public class main {

    private static String graph="<http://lumb/for.sepa.test/workspace/defaultgraph>";
    private static String ontology = "http://lumb/for.sepa.test/ontology";	 
    private static boolean POPOLATE =true;
    private static boolean RUN = false;
    private static boolean CLEAN = false;
    
	public static void main (String[] args) {

		 if(POPOLATE){
			 popolateStore();
		 }
		 
		 if(RUN){

				SparqlRequest update_for_Q2=(SparqlRequest)RequestFactory.getInstance().getRequestByName(RequestName.UPDATE_FOR_Q2.toString());
				UpdateConstruct ar = AddedRemovedGenerator.getAddedRemovedFrom(update_for_Q2);
				if(ar.needDelete()) {
					try {
						System.out.println(AddedRemovedGenerator.generateDeleteUpdate(update_for_Q2,ar).getSparql().getSparql());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(ar.needInsert()) {
					try {
						System.out.println(AddedRemovedGenerator.generateInsertUpdate(update_for_Q2,ar).getSparql().getSparql());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		 }		 
		 
		 if(CLEAN){
			 cleanStore();
		 }
		
	
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
