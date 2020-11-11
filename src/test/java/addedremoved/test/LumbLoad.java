package addedremoved.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.lehigh.swat.bench.uba.Generator;
import edu.lehigh.swat.bench.uba.Ontology;
import factories.IRequestFactory;
import factories.RequestFactory;
import support.Environment;

public class LumbLoad {
	
	private static String graph=Environment.graph;
	private static String ontology =Environment.ontology;	
	private static IRequestFactory factory = null;
	
	@BeforeClass
	public static void init() {
		//instanzio la fattoria di richieste
		factory=RequestFactory.getInstance();
	}

	
	@Test // (timeout = 5000)
	public void test() {
		
		assertTrue("#) Load lumb ontology",Ontology.insertOntology());
		
		//options:
		//   -univ number of universities to generate; 1 by default
		//   -index starting index of the universities; 0 by default
		//   -seed seed used for random data generation; 0 by default
		//   -daml generate DAML+OIL data; OWL data by default
		//   -onto url of the univ-bench ontology
			  
	    int univNum = 2, startIndex = 0, seed = 0;
	    boolean daml = false;
	    assertTrue("#)  Generate and insert lumb.",new Generator().start(univNum, startIndex, seed, daml, ontology,graph));

	
	}
	
}
