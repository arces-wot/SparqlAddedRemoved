package core;
import java.io.IOException;

import connector.IRequestFactory;
import connector.ISparqlRequest;
import connector.RequestFacotry;
import connector.RequestFacotry.RequestName;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import it.unibo.arces.wot.sepa.commons.response.Response;
import it.unibo.arces.wot.sepa.commons.response.UpdateResponse;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import model.EndPoint;
import model.SparqlObj;

public class main {
	
	public static void main (String[] args) {

		 
	    ISparqlRequest r=RequestFacotry.getIntance().getRequestByName(RequestName.SIMPLE_INSERT.toString());
		process(r.execute());
		
		ISparqlRequest q=RequestFacotry.getIntance().getRequestByName(RequestName.SIMPLE_QUERY.toString());
//		process(q.execute());
		
		ISparqlRequest q2=RequestFacotry.getIntance().getRequestByName(RequestName.SIMPLE_QUERY.toString());
//		process(q.execute());
		
		System.out.println("--->"+Inspector.areEq(((QueryResponse) q.execute()).getBindingsResults(),((QueryResponse) q2.execute()).getBindingsResults()));
		
		ISparqlRequest d=RequestFacotry.getIntance().getRequestByName(RequestName.SIMPLE_DELETE.toString());
		process(d.execute());
		
	
		
		q=RequestFacotry.getIntance().getRequestByName(RequestName.SIMPLE_QUERY.toString());
		process(q.execute());
		
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
