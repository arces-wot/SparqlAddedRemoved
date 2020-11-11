package addedremoved.ask;

import java.util.ArrayList;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;

import addedremoved.TripleConverter;
import addedremoved.UpdateExtractedData;
import connector.SparqlRequest;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import model.EndPoint;
import model.SparqlObj;

public class Asks implements IAsk{
	
	

	private ArrayList<UpdateExtractedData> ueds = new ArrayList<UpdateExtractedData> ();
	private SparqlObj sparql;
	private EndPoint endPoint;
	
	public Asks(ArrayList<UpdateExtractedData> ueds,SparqlObj sparql, EndPoint endPoint) {
		this.sparql=sparql;
		this.endPoint=endPoint;
		this.ueds= ueds;
	
	}

	
	
	protected boolean isBindingPresent(Bindings bindings,SparqlObj sparql, EndPoint ep) throws SEPABindingsException {
	
		Triple t = TripleConverter.bindingToTriple(bindings);

		Query ask = new Query();
		ask.setQueryAskType();

		ElementTriplesBlock block = new ElementTriplesBlock();
		block.addTriple(t);
		ask.setQueryPattern(block);

		String askq = ask.serialize();
		SparqlObj askSparql= sparql;
		askSparql.setSparql(askq);
		SparqlRequest askquery = new SparqlRequest(askSparql,ep);

		BindingsResults isPresentResult = ((QueryResponse)askquery.execute()).getBindingsResults();
		return isPresentResult.toJson().get("boolean").getAsBoolean();
	}


	public ArrayList<UpdateExtractedData> filter() throws SEPABindingsException {
		for (UpdateExtractedData constructs : ueds) {
			for(Bindings bindings : constructs.getAdded().getBindings()){
				boolean isPresent = this.isBindingPresent(bindings,sparql,endPoint);
				if(isPresent){
					constructs.removeBingingFromAddedList(bindings);
				}
			}
			for(Bindings bindings : constructs.getRemoved().getBindings()){
				boolean isPresent = this.isBindingPresent(bindings,sparql,endPoint);
				if(!isPresent){
					constructs.removeBingingFromRemovedList(bindings);
				}
			}		
		}
		return ueds;
	}
	
}
