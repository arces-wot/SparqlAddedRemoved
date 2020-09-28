package addedremoved.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import connector.IRequestFactory;
import connector.ISparqlRequest;
import connector.RequestFacotry;
import connector.RequestFacotry.RequestName;
import core.Inspector;
import edu.lehigh.swat.bench.uba.Generator;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;

public class LumbLoadAndTest {
	
	private static String graph="<http://lumb/for.sepa.test/workspace/defaultgraph>";
	private static String ontology = "http://lumb/for.sepa.test/ontology";	
	private static IRequestFactory factory = null;
	
	@BeforeClass
	public static void init() {
		//instanzio la fattoria di richieste
		factory=RequestFacotry.getIntance();
	}

	
	@Test // (timeout = 5000)
	public void test() {
		
		System.out.println("#)  Load lumb kb"); 
		//options:
		//   -univ number of universities to generate; 1 by default
		//   -index starting index of the universities; 0 by default
		//   -seed seed used for random data generation; 0 by default
		//   -daml generate DAML+OIL data; OWL data by default
		//   -onto url of the univ-bench ontology
			  
	    int univNum = 2, startIndex = 0, seed = 0;
	    boolean daml = false;
	   // assertTrue("#)  Generate and insert lumb.",new Generator().start(univNum, startIndex, seed, daml, ontology,graph));

	
	}
	
	@Test
	public void query1() {
		assertFalse("#)  QUERY N°1",((QueryResponse)factory.getRequestByName(RequestName.QUERY1.toString())
				.execute()).isError());
	}
	
	@Test
	public void query2() {
	assertFalse("#)  QUERY N°2",((QueryResponse)factory.getRequestByName(RequestName.QUERY2.toString())
			.execute()).isError());
	}
	
	@Test
	public void query3() {
	assertFalse("#)  QUERY N°3",((QueryResponse)factory.getRequestByName(RequestName.QUERY3.toString())
			.execute()).isError());
	}
	@Test
	public void query4() {
	assertFalse("#)  QUERY N°4",((QueryResponse)factory.getRequestByName(RequestName.QUERY4.toString())
			.execute()).isError());
	}
	@Test
	public void query5() {
	assertFalse("#)  QUERY N°5",((QueryResponse)factory.getRequestByName(RequestName.QUERY5.toString())
			.execute()).isError());
	}
	@Test
	public void query6() {
	assertFalse("#)  QUERY N°6",((QueryResponse)factory.getRequestByName(RequestName.QUERY6.toString())
			.execute()).isError());
	}
	@Test
	public void query7() {
	assertFalse("#)  QUERY N°7",((QueryResponse)factory.getRequestByName(RequestName.QUERY7.toString())
			.execute()).isError());
	}
	@Test
	public void query8() {
	assertFalse("#)  QUERY N°8",((QueryResponse)factory.getRequestByName(RequestName.QUERY8.toString())
			.execute()).isError());
	}
	@Test
	public void query9() {
	assertFalse("#)  QUERY N°9",((QueryResponse)factory.getRequestByName(RequestName.QUERY9.toString())
			.execute()).isError());
	}
	@Test
	public void query10() {
	assertFalse("#)  QUERY N°10",((QueryResponse)factory.getRequestByName(RequestName.QUERY10.toString())
			.execute()).isError());
	}
	@Test
	public void query11() {
	assertFalse("#)  QUERY N°11",((QueryResponse)factory.getRequestByName(RequestName.QUERY11.toString())
			.execute()).isError());
	}
	@Test
	public void query12() {
	assertFalse("#)  QUERY N°12",((QueryResponse)factory.getRequestByName(RequestName.QUERY12.toString())
			.execute()).isError());
	}
	@Test
	public void query13() {
	assertFalse("#)  QUERY N°13",((QueryResponse)factory.getRequestByName(RequestName.QUERY13.toString())
			.execute()).isError());
	}
	
	@Test
	public void query14() {
	assertFalse("#)  QUERY N°14",((QueryResponse)factory.getRequestByName(RequestName.QUERY14.toString())
			.execute()).isError());
	}
}
