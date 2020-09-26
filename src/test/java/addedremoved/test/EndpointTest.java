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
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;

public class EndpointTest {

	private static IRequestFactory factory = null;
	@BeforeClass
	public static void init() {
		//instanzio la fattoria di richieste
		factory=RequestFacotry.getIntance();
	}

	
	@Test // (timeout = 5000)
	public void test() {
		
		System.out.println("#)  EndpointTest SIMPLE_INSERT"); 
	    ISparqlRequest req=factory.getRequestByName(RequestName.SIMPLE_INSERT.toString());
	    assertFalse(req.execute().isError());

		System.out.println("#)  EndpointTest SIMPLE_QUERY"); 
		req=factory.getRequestByName(RequestName.SIMPLE_QUERY.toString());
		assertFalse(Inspector.isVoid(((QueryResponse)req.execute()).getBindingsResults()));
		
		System.out.println("#)  EndpointTest SIMPLE_DELETE"); 
		req=factory.getRequestByName(RequestName.SIMPLE_DELETE.toString());
		assertFalse(req.execute().isError());
		
		System.out.println("#)  EndpointTest SIMPLE_QUERY (for confirm delete)");
		req=factory.getRequestByName(RequestName.SIMPLE_QUERY.toString());
		assertTrue(Inspector.isVoid(((QueryResponse)req.execute()).getBindingsResults()));
		
	}
	
}
