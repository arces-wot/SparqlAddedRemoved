package addedremoved.epspec;

import java.util.ArrayList;

import addedremoved.UpdateExtractedData;
import addedremoved.ask.AsksAsSelectExistsList;
import addedremoved.ask.AsksAsSelectGraphAsVar;
import addedremoved.ask.IAsk;
import addedremoved.epspec.EpSpecFactory.EndPointSpec;
import model.EndPoint;
import model.SparqlObj;


public class VirtuosoSpecification implements IEndPointSpecification {

	public boolean asksAsSelectExistListCompare(String value) {
		return value.compareTo("1")==0;
	}

	public String s() {
		// TODO Auto-generated method stub
		return "s";
	}

	public String p() {
		// TODO Auto-generated method stub
		return "p";
	}

	public String o() {
		// TODO Auto-generated method stub
		return "o";
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
		return EndPointSpec.VIRTUOSO;
	}

	@Override
	public IAsk getAsk(ArrayList<UpdateExtractedData> ueds, SparqlObj sparql, EndPoint endPoint){		
		return new AsksAsSelectExistsList(ueds, sparql, endPoint);//AsksAsSelectGraphAsVar(ueds, sparql, endPoint);
	}


}
