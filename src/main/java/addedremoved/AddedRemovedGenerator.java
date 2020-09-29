package addedremoved;

import java.util.Set;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;

import com.google.gson.JsonObject;

import connector.SparqlRequest;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.request.QueryRequest;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import it.unibo.arces.wot.sepa.timing.Timings;
import model.AddedRemoved;
import model.EndPoint;
import model.SparqlObj;

public class AddedRemovedGenerator {
	
	
			private static void generateInsertDeleteUpdates(SparqlObj sparql, EndPoint ep) {
				try {
					AddedRemoved ar =GetAddedRemovedTriples(sparql,ep);
					
				} catch (SEPASecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SEPABindingsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
			
			private static boolean isBindingPresent(Bindings bindings,SparqlObj sparql, EndPoint ep) throws SEPABindingsException, SEPASecurityException {
			
				Triple t = bindingToTriple(bindings);

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

			private static Triple bindingToTriple(Bindings bindings) throws SEPABindingsException{
				String subject = bindings.getValue("s");
				String predicate = bindings.getValue("p");
				String object = bindings.getValue("o");			
				
				Node s = bindings.isBNode("s") ? NodeFactory.createBlankNode(subject) : NodeFactory.createURI(subject);
				Node p = bindings.isBNode("p") ? NodeFactory.createBlankNode(predicate) : NodeFactory.createURI(predicate);

				Node o = null;
				if(!bindings.isBNode("o")){
					o = bindings.isURI("o") ? NodeFactory.createURI(object) : NodeFactory.createLiteral(object);
				}else{
					o = NodeFactory.createBlankNode(object);
				}

				return new Triple(s,p,o);
			}

	
			
			private static AddedRemoved GetAddedRemovedTriples(SparqlObj sparql, EndPoint ep ) throws SEPASecurityException, SEPABindingsException {
				long start = Timings.getTime();
				SPARQLAnalyzer sa = new SPARQLAnalyzer(sparql.getSparql());
				UpdateConstruct constructs = sa.getConstruct();

				
				BindingsResults added =  new BindingsResults(new JsonObject());
				BindingsResults removed =  new BindingsResults(new JsonObject());

				String dc = constructs.getDeleteConstruct();
				
				if (dc.length() > 0) {				
					SparqlObj getRremovedSparql = sparql;
					getRremovedSparql.setSparql(dc);
					removed = ((QueryResponse) new SparqlRequest(getRremovedSparql,ep).execute()).getBindingsResults();
				}

				String ac = constructs.getInsertConstruct();		
				if (ac.length() > 0) {
					SparqlObj getAddedSparql = sparql;
					getAddedSparql.setSparql(ac);
					added  = ((QueryResponse) new SparqlRequest(getAddedSparql,ep).execute()).getBindingsResults();
					
				}

				for(Bindings bindings : added.getBindings()){
					boolean isPresent = isBindingPresent( bindings,sparql,ep);
					if(isPresent){
						added.getBindings().remove(bindings);
					}
				}

				for(Bindings bindings : removed.getBindings()){
					boolean isPresent = isBindingPresent(bindings,sparql,ep);
					if(!isPresent){
						removed.getBindings().remove(bindings);
					}
				}
				long stop = System.currentTimeMillis();
				
				return new AddedRemoved(added,removed);
			}
}
