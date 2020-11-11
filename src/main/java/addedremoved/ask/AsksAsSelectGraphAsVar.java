package addedremoved.ask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import addedremoved.BindingTag;
import addedremoved.UpdateExtractedData;
import connector.SparqlRequest;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import model.EndPoint;
import model.SparqlObj;

public class AsksAsSelectGraphAsVar implements IAsk{
	
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

	private String removedAsksAsSelect=null;
	private String addedAsksAsSelect=null;
	private ArrayList<UpdateExtractedData> ueds = new ArrayList<UpdateExtractedData> ();
	private SparqlObj sparql;
	private EndPoint endPoint;
	
	public AsksAsSelectGraphAsVar(ArrayList<UpdateExtractedData> ueds, SparqlObj sparql, EndPoint endPoint) {
		this.ueds=ueds;
		this.sparql=sparql;
		this.endPoint=endPoint;
		this.init();
		
	}
	
	protected void init() {
		String added="";
		String removed="";
		for (UpdateExtractedData updateConstruct : ueds) {
			if(updateConstruct.needDelete()) {
				String deleteGraph= updateConstruct.getRemovedGraph();
				for (Bindings bind : updateConstruct.getRemoved().getBindings()) {
					removed+=incapsulate(deleteGraph,bind);
				}
			}
			if(updateConstruct.needInsert()) {
				String addedGraph= updateConstruct.getAddedGraph();
				for (Bindings bind : updateConstruct.getAdded().getBindings()) {
					added+=incapsulate(addedGraph,bind);
				}
			}
		}
		if(removed.length()>0) {
			removedAsksAsSelect=generateSelect(removed);
		}
		if(added.length()>0) {
			addedAsksAsSelect=generateSelect(added);
		}
	}

	public String getAsksAsSelect(HashMap<String,ArrayList<Bindings>> allTriple) {
		String select = "SELECT ?"+BindingTag.GRAPH.toString()
		+" ?"+BindingTag.SUBJECT.toString()
		+" ?"+BindingTag.PREDICATE.toString()
		+" ?"+BindingTag.OBJECT.toString()
		+" { GRAPH ?"+BindingTag.GRAPH.toString()
		+" { ?"+BindingTag.SUBJECT.toString()
		+" ?"+BindingTag.PREDICATE.toString()
		+" ?"+BindingTag.OBJECT.toString()
		+" }\n VALUES (?"+BindingTag.GRAPH.toString()
		+" ?"+BindingTag.SUBJECT.toString()
		+" ?"+BindingTag.PREDICATE.toString()
		+" ?"+BindingTag.OBJECT.toString()+") { \n";
		for (String graph : allTriple.keySet()) {
			for (Bindings bind : allTriple.get(graph)) {
				select+="(<"+graph+"><"+bind.getValue(BindingTag.SUBJECT.toString())+"><"
						+bind.getValue(BindingTag.PREDICATE.toString())+"><"
						+bind.getValue(BindingTag.OBJECT.toString())+">)\n";
			}
		}
		select+="}}";
		return select;
	}
	
	protected String generateSelect(String values) {
		return "SELECT ?"+BindingTag.GRAPH.toString()
				+" ?"+BindingTag.SUBJECT.toString()
				+" ?"+BindingTag.PREDICATE.toString()
				+" ?"+BindingTag.OBJECT.toString()
				+" { GRAPH ?"+BindingTag.GRAPH.toString()
				+" { ?"+BindingTag.SUBJECT.toString()
				+" ?"+BindingTag.PREDICATE.toString()
				+" ?"+BindingTag.OBJECT.toString()
				+" }\n VALUES (?"+BindingTag.GRAPH.toString()
				+" ?"+BindingTag.SUBJECT.toString()
				+" ?"+BindingTag.PREDICATE.toString()
				+" ?"+BindingTag.OBJECT.toString()+") { \n" + values+ "}}";
	}
	
	protected String incapsulate(String graph,Bindings bind ) {
		return "(<"+graph+"><"+bind.getValue(BindingTag.SUBJECT.toString())+"><"
				+bind.getValue(BindingTag.PREDICATE.toString())+"><"
				+bind.getValue(BindingTag.OBJECT.toString())+">)\n";
	}
	


	
	public BindingsResults getBindingsForRemoved()  {
	
	
		SparqlObj askSparql= sparql;
		askSparql.setSparql(removedAsksAsSelect);
		SparqlRequest askquery = new SparqlRequest(askSparql,endPoint);
		return ((QueryResponse)askquery.execute()).getBindingsResults();
		
	}
	
	
	public BindingsResults getBindingsForAdded()  {
		
		
		SparqlObj askSparql= sparql;
		askSparql.setSparql(addedAsksAsSelect);
		SparqlRequest askquery = new SparqlRequest(askSparql,endPoint);
		return ((QueryResponse)askquery.execute()).getBindingsResults();
		
	}
	
	
	protected HashMap<String,BindingsResults> getReorganizedBindingsForAdded() throws SEPABindingsException  {
		
		HashMap<String,BindingsResults>  list = new HashMap<String,BindingsResults>();
		
		ArrayList<String> vars = new ArrayList<String>();
		vars.add(BindingTag.SUBJECT.toString());
		vars.add(BindingTag.PREDICATE.toString());
		vars.add(BindingTag.OBJECT.toString());
		
		if(needAskSelectForAdded()) {
//			TestMetric t= new TestMetric("");
//			t.start();
			BindingsResults result = getBindingsForAdded();
//			t.stop();
//			System.out.println("real call: "+t.getInterval());
			for (Bindings bind : result.getBindings()) {
				String graph = bind.getValue(BindingTag.GRAPH.toString());
				Bindings triple = new Bindings();
				triple.addBinding(BindingTag.SUBJECT.toString(), bind.getRDFTerm(BindingTag.SUBJECT.toString()));
				triple.addBinding(BindingTag.PREDICATE.toString(), bind.getRDFTerm(BindingTag.PREDICATE.toString()));
				triple.addBinding(BindingTag.OBJECT.toString(), bind.getRDFTerm(BindingTag.OBJECT.toString()));
				
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
	
	
	protected  HashMap<String,BindingsResults> getReorganizedBindingsForRemoved() throws SEPABindingsException  {
		
		HashMap<String,BindingsResults>  list = new HashMap<String,BindingsResults>();
		
		ArrayList<String> vars = new ArrayList<String>();
		vars.add(BindingTag.SUBJECT.toString());
		vars.add(BindingTag.PREDICATE.toString());
		vars.add(BindingTag.OBJECT.toString());
		
		if(needAskSelectForRemoved()) {
			BindingsResults result = getBindingsForRemoved();
			for (Bindings bind : result.getBindings()) {
				String graph = bind.getValue(BindingTag.GRAPH.toString());
				Bindings triple = new Bindings();
				triple.addBinding(BindingTag.SUBJECT.toString(), bind.getRDFTerm(BindingTag.SUBJECT.toString()));
				triple.addBinding(BindingTag.PREDICATE.toString(), bind.getRDFTerm(BindingTag.PREDICATE.toString()));
				triple.addBinding(BindingTag.OBJECT.toString(), bind.getRDFTerm(BindingTag.OBJECT.toString()));
				
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
	
	

	public ArrayList<UpdateExtractedData> filter() throws SEPABindingsException {

		HashMap<String,BindingsResults> alredyExist  = this.getReorganizedBindingsForAdded();
		HashMap<String,BindingsResults> realRemoved  = this.getReorganizedBindingsForRemoved();
		for (UpdateExtractedData constructs : ueds) {
			
			String graph = constructs.getAddedGraph();
			
			if(constructs.needInsert() && alredyExist.containsKey(graph) ){
				constructs.removeBingingFromAddedList(alredyExist.get(graph)); 

				alredyExist.remove(graph);
			}	
			
			graph = constructs.getRemovedGraph();
			
			if(realRemoved.containsKey(graph)) {
				constructs.setRemoved(realRemoved.get(graph));
				
				realRemoved.remove(graph);
			}else {
				constructs.clearRemoved();
			}
			
		}
		
		
		return ueds;
	}
	
	
	//----------------------------------------------------------------------SETTERS and GETTERS
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
