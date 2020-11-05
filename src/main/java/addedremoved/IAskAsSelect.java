package addedremoved;

import java.util.HashMap;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import model.EndPoint;
import model.SparqlObj;

public interface IAskAsSelect {

	
	public HashMap<String,BindingsResults> getReorganizedBindingsForRemoved(SparqlObj sparql, EndPoint ep)  throws SEPABindingsException ;
	public HashMap<String,BindingsResults> getReorganizedBindingsForAdded(SparqlObj sparql, EndPoint ep)  throws SEPABindingsException ;
	
	public boolean needAskSelectForAdded();
	
	public boolean needAskSelectForRemoved();
}
