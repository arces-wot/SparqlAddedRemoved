package connector;


import java.util.ArrayList;

import factories.RequestFactory;
import factories.RequestName;
import model.EndPoint;
import model.SparqlObj;

public class CleanerRDFStore {

	public static boolean clean() {
		return !RequestFactory.getInstance().getRequestByName(RequestName.SIMPLE_DELETE).execute().isError();
	}
	
	public static boolean clean(ArrayList<String> graphs) {
		boolean allGood = true;
		for (String graph : graphs) {
			String fixedGraph =graph.trim();
			if(fixedGraph.charAt(0)!='<'){
				fixedGraph="<"+fixedGraph+">";
			}
			EndPoint endPointHost= new EndPoint("http","localhost",8000,"/update");
			if(new SparqlRequest(new SparqlObj("CLEAR GRAPH" + fixedGraph),endPointHost).execute().isError()) {
				allGood=false;
			}
		}
		if(!clean()){
			return false;
		}		
		return allGood;
	}
}
