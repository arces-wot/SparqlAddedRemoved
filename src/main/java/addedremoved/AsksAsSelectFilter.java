package addedremoved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.reasoner.TriplePattern;
import org.apache.jena.shacl.engine.constraint.SparqlComponent;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;

import connector.SparqlRequest;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import model.EndPoint;
import model.SparqlObj;
import model.TestMetric;

public class AsksAsSelectFilter implements IAskAsSelect{
	
	/*
	 * Example:
	 * 
		SELECT  ?s ?p ?o WHERE{
			GRAPH <prova3>{
				?s ?p  ?o.
			}
			FILTER( 
				?s = <s1> &&  ?p = <P> && ?o = <o1> ||
				?s = <s2> &&  ?p = <P> && ?o = <o1> 
			)
		}


	 */

	public HashMap<String,String> removedAsksAsSelect=null;
	public HashMap<String,String> addedAsksAsSelect=null;

	
	public AsksAsSelectFilter(ArrayList<UpdateConstruct> constructsList) {
		for (UpdateConstruct updateConstruct : constructsList) {
			if(updateConstruct.needDelete()) {
				String deleteGraph= updateConstruct.getRemovedGraph();
				for (Bindings bind : updateConstruct.getRemoved().getBindings()) {
					putInRemoved(deleteGraph,incapsulateWithExplicitGraph(bind));
				}
			}
			if(updateConstruct.needInsert()) {
				String addedGraph= updateConstruct.getAddedGraph();
				for (Bindings bind : updateConstruct.getAdded().getBindings()) {
					putInAdded(addedGraph,incapsulateWithExplicitGraph(bind));
				}
			}
		}
		if(needAskSelectForRemoved()) {
			for (String graph : removedAsksAsSelect.keySet()) {
				removedAsksAsSelect.put(graph,generateSelectWithExplicitGraph(graph,removedAsksAsSelect.get(graph)));
			}
		}
		if(needAskSelectForAdded()) {
			for (String graph : addedAsksAsSelect.keySet()) {
				addedAsksAsSelect.put(graph,generateSelectWithExplicitGraph(graph,addedAsksAsSelect.get(graph)));
			}
		}
	}

	private void putInAdded(String graph,String added) {
			if(removedAsksAsSelect==null) {
				removedAsksAsSelect= new  HashMap<String,String>();
			}
			if(removedAsksAsSelect.containsKey(graph)) {
				removedAsksAsSelect.put(graph,removedAsksAsSelect.get(graph)+added);
			}else {
				removedAsksAsSelect.put(graph,added);
			}			
	}
	private void putInRemoved(String graph,String added) {
		if(addedAsksAsSelect==null) {
			addedAsksAsSelect= new  HashMap<String,String>();
		}
		if(addedAsksAsSelect.containsKey(graph)) {
			addedAsksAsSelect.put(graph,addedAsksAsSelect.get(graph)+added);
		}else {
			addedAsksAsSelect.put(graph,added);
		}			
	}

	private String generateSelectWithExplicitGraph(String graph,String values) {
		return "SELECT ?s ?p ?o WHERE { GRAPH <"+graph+"> { ?s ?p ?o }\n FILTER (\\n\" + values+ \") }";
	}
	
	private String incapsulateWithExplicitGraph(Bindings bind ) {
		return "?s == <"+bind.getValue("s")+">  && ?p== <"+bind.getValue("p")+"> && ?o == <"+bind.getValue("o")+">\n";
	}

	
	public BindingsResults getBindingsForRemoved(SparqlObj sparql, EndPoint ep)  {
		BindingsResults ris =null;
		for (String  graph : removedAsksAsSelect.keySet()) {
			SparqlObj askSparql= sparql;
			askSparql.setSparql(removedAsksAsSelect.get(graph));
			SparqlRequest askquery = new SparqlRequest(askSparql,ep);
			if(ris==null) {
				ris=((QueryResponse)askquery.execute()).getBindingsResults();
			}else {
				for(Bindings br :((QueryResponse)askquery.execute()).getBindingsResults().getBindings()){
					ris.add(br);
				}
			}
		}
		return ris;
		
		
	}
	
	
	public BindingsResults getBindingsForAdded(SparqlObj sparql, EndPoint ep)  {
		
		BindingsResults ris =null;
		for (String  graph : addedAsksAsSelect.keySet()) {
			SparqlObj askSparql= sparql;
			askSparql.setSparql(addedAsksAsSelect.get(graph));
			SparqlRequest askquery = new SparqlRequest(askSparql,ep);
			if(ris==null) {
				ris=((QueryResponse)askquery.execute()).getBindingsResults();
			}else {
				for(Bindings br :((QueryResponse)askquery.execute()).getBindingsResults().getBindings()){
					ris.add(br);
				}
			}
		}
		return ris;
		
	}
	
	
	public HashMap<String,BindingsResults> getReorganizedBindingsForAdded(SparqlObj sparql, EndPoint ep) throws SEPABindingsException  {
		
		HashMap<String,BindingsResults>  list = new HashMap<String,BindingsResults>();
		
		ArrayList<String> vars = new ArrayList<String>();
		vars.add("s");
		vars.add("p");
		vars.add("o");
		
		if(needAskSelectForAdded()) {
//			TestMetric t= new TestMetric("");
//			t.start();
			BindingsResults result = getBindingsForAdded(sparql, ep);
//			t.stop();
//			System.out.println("real call: "+t.getInterval());
			for (Bindings bind : result.getBindings()) {
				String graph = bind.getValue("g");
				Bindings triple = new Bindings();
				triple.addBinding("s", bind.getRDFTerm("s"));
				triple.addBinding("p", bind.getRDFTerm("p"));
				triple.addBinding("o", bind.getRDFTerm("o"));
				
				if(list.containsKey(graph)) {
					list.get(graph).add(triple);
				}else {
					ArrayList<Bindings> bindList = new ArrayList<Bindings>();
					bindList.add(triple);
					list.put(graph,new BindingsResults(vars, bindList));	
				}
			}
		}
		return list;		
		
	}
	
	
	public HashMap<String,BindingsResults> getReorganizedBindingsForRemoved(SparqlObj sparql, EndPoint ep) throws SEPABindingsException  {
		
		HashMap<String,BindingsResults>  list = new HashMap<String,BindingsResults>();
		
		ArrayList<String> vars = new ArrayList<String>();
		vars.add("s");
		vars.add("p");
		vars.add("o");
		
		if(needAskSelectForRemoved()) {
			BindingsResults result = getBindingsForRemoved(sparql, ep);
			for (Bindings bind : result.getBindings()) {
				String graph = bind.getValue("g");
				Bindings triple = new Bindings();
				triple.addBinding("s", bind.getRDFTerm("s"));
				triple.addBinding("p", bind.getRDFTerm("p"));
				triple.addBinding("o", bind.getRDFTerm("o"));
				
				if(list.containsKey(graph)) {
					list.get(graph).add(triple);
				}else {
					ArrayList<Bindings> bindList = new ArrayList<Bindings>();
					bindList.add(triple);
					list.put(graph,new BindingsResults(vars, bindList));	
				}
			}
		}
		return list;		
		
	}
	
	
	
	public boolean needAskSelectForAdded() {
		return addedAsksAsSelect!=null && addedAsksAsSelect.size()>0 ;
	}
	
	public boolean needAskSelectForRemoved() {
		return removedAsksAsSelect!=null&& removedAsksAsSelect.size()>0;
	}
	
}
