package addedremoved.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import connector.ISparqlRequest;
import core.Inspector;
import edu.lehigh.swat.bench.uba.Generator;
import edu.lehigh.swat.bench.uba.Ontology;
import factories.IRequestFactory;
import factories.RequestFactory;
import factories.RequestName;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import support.Environment;

public class LumbTestOnly {
	
	private static String graph=Environment.graph;
	private static String ontology =Environment.ontology;	
	private static IRequestFactory factory = null;
	
	@BeforeClass
	public static void init() {
		//instanzio la fattoria di richieste
		factory=RequestFactory.getInstance();
	}

	
	
	
	@Test
	public void query1() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY1).execute());
		assertFalse("#)  QUERY N°1",res.isError());
		System.out.println("Query N°1: "+ res.getBindingsResults().size()+ " Triple.");
	}
	
	@Test
	public void query2() {	
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY2).execute());
		assertFalse("#)  QUERY N°2",res.isError());
		System.out.println("Query N°2: "+ res.getBindingsResults().size()+ " Triple.");
	}
	
	@Test
	public void query3() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY3).execute());
		assertFalse("#)  QUERY N°3",res.isError());
		System.out.println("Query N°3: "+ res.getBindingsResults().size()+ " Triple.");
	}
	@Test
	public void query4() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY4).execute());
		assertFalse("#)  QUERY N°4",res.isError());
		System.out.println("Query N°4: "+ res.getBindingsResults().size()+ " Triple.");
	}
	@Test
	public void query5() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY5).execute());
		assertFalse("#)  QUERY N°5",res.isError());
		System.out.println("Query N°5: "+ res.getBindingsResults().size()+ " Triple.");
	}
	@Test
	public void query6() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY6).execute());
		assertFalse("#)  QUERY N°6",res.isError());
		System.out.println("Query N°6: "+ res.getBindingsResults().size()+ " Triple.");
	}
	@Test
	public void query7() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY7).execute());
		assertFalse("#)  QUERY N°7",res.isError());
		System.out.println("Query N°7: "+ res.getBindingsResults().size()+ " Triple.");
	}
	@Test
	public void query8() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY8).execute());
		assertFalse("#)  QUERY N°8",res.isError());
		System.out.println("Query N°8: "+ res.getBindingsResults().size()+ " Triple.");
	}
	@Test
	public void query9() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY9).execute());
		assertFalse("#)  QUERY N°9",res.isError());
		System.out.println("Query N°9: "+ res.getBindingsResults().size()+ " Triple.");
	}
	@Test
	public void query10() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY10).execute());
		assertFalse("#)  QUERY N°10",res.isError());
		System.out.println("Query N°10: "+ res.getBindingsResults().size()+ " Triple.");
	}
	@Test
	public void query11() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY11).execute());
		assertFalse("#)  QUERY N°11",res.isError());
		System.out.println("Query N°11: "+ res.getBindingsResults().size()+ " Triple.");
	}
	@Test
	public void query12() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY12).execute());
		assertFalse("#)  QUERY N°12",res.isError());
		System.out.println("Query N°12: "+ res.getBindingsResults().size()+ " Triple.");
	}
	@Test
	public void query13() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY13).execute());
		assertFalse("#)  QUERY N°13",res.isError());
		System.out.println("Query N°13: "+ res.getBindingsResults().size()+ " Triple.");
	}
	
	@Test
	public void query14() {
		QueryResponse res=	((QueryResponse)factory.getRequestByName(RequestName.QUERY14).execute());
		assertFalse("#)  QUERY N°14",res.isError());
		System.out.println("Query N°14: "+ res.getBindingsResults().size()+ " Triple.");
	}
}
