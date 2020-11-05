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

public class AsksAsSelectGraphAsVar implements IAskAsSelect{
	
	/*
	 * Example:
	 * 
			SELECT ?g ?s ?p ?o {
					GRAPH ?g {  
						?s ?p ?o.
					}
				VALUES (?g ?s ?p ?o) {
					  (<prova3><s> <p> <o>)
					  (<prova3><s1>	<P> <o1>)
					  (<prova2><s2> <P> <o2>)
				}
			}

	 */

	public String removedAsksAsSelect=null;
	public String addedAsksAsSelect=null;

	
	public AsksAsSelectGraphAsVar(ArrayList<UpdateConstruct> constructsList) {
		String added="";
		String removed="";
		for (UpdateConstruct updateConstruct : constructsList) {
			if(updateConstruct.needDelete()) {
				String deleteGraph= updateConstruct.getRemovedGraph();
				for (Bindings bind : updateConstruct.getRemoved().getBindings()) {
					removed+=incapsulateWithGraphAsVar(deleteGraph,bind);
				}
			}
			if(updateConstruct.needInsert()) {
				String addedGraph= updateConstruct.getAddedGraph();
				for (Bindings bind : updateConstruct.getAdded().getBindings()) {
					added+=incapsulateWithGraphAsVar(addedGraph,bind);
				}
			}
		}
		if(removed.length()>0) {
			removedAsksAsSelect=generateSelectWithGraphAsVar(removed);
		}
		if(added.length()>0) {
			addedAsksAsSelect=generateSelectWithGraphAsVar(added);
		}
	}

	public String getAsksAsSelect(HashMap<String,ArrayList<Bindings>> allTriple) {
		String select = "SELECT ?g ?s ?p ?o { GRAPH ?g { ?s ?p ?o }\n VALUES (?g ?s ?p ?o) { \n";
		for (String graph : allTriple.keySet()) {
			for (Bindings bind : allTriple.get(graph)) {
				select+="(<"+graph+"><"+bind.getValue("s")+"><"+bind.getValue("p")+"><"+bind.getValue("o")+">)\n";
			}
		}
		select+="}}";
		return select;
	}
	
	private String generateSelectWithGraphAsVar(String values) {
		return "SELECT ?g ?s ?p ?o { GRAPH ?g { ?s ?p ?o }\n VALUES (?g ?s ?p ?o) { \n" + values+ "}}";
	}
	
	private String incapsulateWithGraphAsVar(String graph,Bindings bind ) {
		return "(<"+graph+"><"+bind.getValue("s")+"><"+bind.getValue("p")+"><"+bind.getValue("o")+">)\n";
	}
	


	
	public BindingsResults getBindingsForRemoved(SparqlObj sparql, EndPoint ep)  {
	
	
		SparqlObj askSparql= sparql;
		askSparql.setSparql(removedAsksAsSelect);
		SparqlRequest askquery = new SparqlRequest(askSparql,ep);
		return ((QueryResponse)askquery.execute()).getBindingsResults();
		
	}
	
	
	public BindingsResults getBindingsForAdded(SparqlObj sparql, EndPoint ep)  {
		
		
		SparqlObj askSparql= sparql;
		askSparql.setSparql(addedAsksAsSelect);
		SparqlRequest askquery = new SparqlRequest(askSparql,ep);
		return ((QueryResponse)askquery.execute()).getBindingsResults();
		
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
	
	public String getRemovedAsksAsSelect() {
		return removedAsksAsSelect;
	}

	public String getAddedAsksAsSelect() {
		return addedAsksAsSelect;
	}
	
	public boolean needAskSelectForAdded() {
		return addedAsksAsSelect!=null;
	}
	
	public boolean needAskSelectForRemoved() {
		return removedAsksAsSelect!=null;
	}
	
}
