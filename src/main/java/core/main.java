package core;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.glassfish.grizzly.http.io.OutputBuffer;

import connector.IRequestFactory;
import connector.ISparqlRequest;
import connector.RequestFacotry;
import connector.RequestFacotry.RequestName;
import edu.lehigh.swat.bench.uba.Generator;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import it.unibo.arces.wot.sepa.commons.response.Response;
import it.unibo.arces.wot.sepa.commons.response.UpdateResponse;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import model.EndPoint;
import model.SparqlObj;

public class main {
	
	public static void main (String[] args) {

		 
		//options:
	//   -univ number of universities to generate; 1 by default
	//   -index starting index of the universities; 0 by default
	//   -seed seed used for random data generation; 0 by default
	//   -daml generate DAML+OIL data; OWL data by default
	//   -onto url of the univ-bench ontology
		  
	    int univNum = 2, startIndex = 0, seed = 0;
	    boolean daml = false;
	    String ontology = "http://lumb/for.sepa.test";

	    HashMap<String,OutputStream> streams=new  HashMap<String,OutputStream>();
	   
	    new Generator().start(univNum, startIndex, seed, daml, ontology,streams);
		System.out.print("streams.keySet().size(): "+ streams.keySet().size());
		
		Model model = ModelFactory.createDefaultModel() ;
		//System.out.println(new String(((ByteArrayOutputStream )streams.values().iterator().next()).toByteArray()).substring(0,1000));
		InputStream in = new ByteArrayInputStream(((ByteArrayOutputStream )streams.values().iterator().next()).toByteArray());
		model.read(in,"RDF/XML");
		
		ByteArrayOutputStream temp = new ByteArrayOutputStream();
		model.write(temp, "N-TRIPLE");

		System.out.print( new String(temp.toByteArray()));
	
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

}
