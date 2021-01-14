package addedremoved.epspec;

import java.util.ArrayList;

import addedremoved.UpdateExtractedData;
import addedremoved.ask.IAsk;
import addedremoved.epspec.EpSpecFactory.EndPointSpec;
import model.EndPoint;
import model.SparqlObj;


public interface IEndPointSpecification {

	public boolean asksAsSelectExistListCompare(String value);
	
	public String s();	
	public String p();
	public String o();
	public String g();
	public ArrayList<String> vars();
	public IAsk getAsk(ArrayList<UpdateExtractedData> ueds, SparqlObj sparql, EndPoint endPoint);
	public EndPointSpec getEndPointName();
	
}
