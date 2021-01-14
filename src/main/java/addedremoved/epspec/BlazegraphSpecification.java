package addedremoved.epspec;

import java.util.ArrayList;

import addedremoved.UpdateExtractedData;
import addedremoved.ask.AsksAsSelectExistsList;
import addedremoved.ask.IAsk;
import addedremoved.epspec.EpSpecFactory.EndPointSpec;
import model.EndPoint;
import model.SparqlObj;



public class BlazegraphSpecification implements IEndPointSpecification {

	public boolean asksAsSelectExistListCompare(String value) {
		return value.compareTo("true")==0;
	}
	
	public String s() {
		// TODO Auto-generated method stub
		return "subject";
	}

	public String p() {
		// TODO Auto-generated method stub
		return "predicate";
	}

	public String o() {
		// TODO Auto-generated method stub
		return "object";
	}

	public String g() {
		// TODO Auto-generated method stub
		return "g";
	}
	
	public ArrayList<String> vars() {
		ArrayList<String> vars = new ArrayList<String>();
		vars.add(s());
		vars.add(p());
		vars.add(o());
		return vars;
	}
	public EndPointSpec getEndPointName() {
		return EndPointSpec.BLAZEGRAPH;
	}


	@Override
	public IAsk getAsk(ArrayList<UpdateExtractedData> ueds, SparqlObj sparql, EndPoint endPoint){	
		return new AsksAsSelectExistsList(ueds, sparql, endPoint);
	}

}
