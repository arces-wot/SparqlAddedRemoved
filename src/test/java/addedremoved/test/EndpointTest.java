package addedremoved.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import connector.ISparqlRequest;
import core.Inspector;
import factories.IRequestFactory;
import factories.RequestFactory;
import factories.RequestName;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;

public class EndpointTest {

	private static IRequestFactory factory = null;
	@BeforeClass
	public static void init() {
		//instanzio la fattoria di richieste
		factory=RequestFactory.getInstance();
	}

	
	@Test // (timeout = 5000)
	public void test() {
		
	    ISparqlRequest req=factory.getRequestByName(RequestName.SIMPLE_INSERT);
	    assertFalse("#)  EndpointTest SIMPLE_INSERT",req.execute().isError());

		req=factory.getRequestByName(RequestName.SIMPLE_QUERY);
		assertFalse("#)  EndpointTest SIMPLE_QUERY",Inspector.isVoid(((QueryResponse)req.execute()).getBindingsResults()));
		
		req=factory.getRequestByName(RequestName.SIMPLE_DELETE);
		assertFalse("#)  EndpointTest SIMPLE_DELETE",req.execute().isError());
		
		req=factory.getRequestByName(RequestName.SIMPLE_QUERY);
		assertTrue("#)  EndpointTest SIMPLE_QUERY (for confirm delete)",Inspector.isVoid(((QueryResponse)req.execute()).getBindingsResults()));
		
	}
	
}
