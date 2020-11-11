package addedremoved.ask;

import java.util.ArrayList;
import java.util.HashMap;

import addedremoved.UpdateExtractedData;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import model.EndPoint;
import model.SparqlObj;

public interface IAsk {
	
	public ArrayList<UpdateExtractedData> filter() throws SEPABindingsException;
}
