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

public class AsksAsSelectExistsList implements IAskAsSelect{
	
	/*
	 * Example:
	 * 
			
		SELECT ?x {
			VALUES (?g ?s ?p ?o ) {
				(<prova3><s> <p> <o> )
				(<prova3><s1> <P> <o1> )
				(<prova2><s2> <P> <o2> )
			}
			BIND(EXISTS{GRAPH ?g {?s ?p ?o}} AS ?x)
		}

	 */

	private HashMap<Integer,BindingsWrapper> tripleList;
	private HashMap<String,BindingsResults>  added=null;
	private HashMap<String,BindingsResults>  removed=null;
	
	
	public AsksAsSelectExistsList(ArrayList<UpdateConstruct> constructsList,SparqlObj sparql, EndPoint ep) {
		int orderIndex = 0;
		tripleList= new HashMap<Integer,BindingsWrapper>();	
		removed= new HashMap<String,BindingsResults>();
		added= new HashMap<String,BindingsResults>();
		String values = "";
		for (UpdateConstruct updateConstruct : constructsList) {
			if(updateConstruct.needDelete()) {
				String deleteGraph= updateConstruct.getRemovedGraph();
				for (Bindings bind : updateConstruct.getRemoved().getBindings()) {
					tripleList.put(orderIndex,new BindingsWrapper(bind,orderIndex,false,deleteGraph));
					values+=incapsulate(deleteGraph,bind,orderIndex);
					orderIndex++;
				}
			}
			if(updateConstruct.needInsert()) {
				String addedGraph= updateConstruct.getAddedGraph();
				for (Bindings bind : updateConstruct.getAdded().getBindings()) {
					tripleList.put(orderIndex,new BindingsWrapper(bind,orderIndex,false,addedGraph));
					values+=incapsulate(addedGraph,bind,orderIndex);
					orderIndex++;
				}
			}
		}
		if(orderIndex>0) {
			for (Bindings bind : getBindings(generateSelectExistsList(values),sparql,ep).getBindings()) {
				BindingsWrapper temp =tripleList.get(Integer.parseInt(bind.getValue("i")));
				if(bind.getValue("x").compareTo("1")==0) {
					if(temp.isAdded()) {
						addToAdded(temp.getGraph(), temp.getBind());
					}else {
						//è una tripla di tipo REMOVED e risulta presente sulla KB
						addToRemoved(temp.getGraph(), temp.getBind());
					}
				}
			}
		}
	}

	private void addToAdded(String graph, Bindings bind) {
		if(added.containsKey(graph)) {
			added.get(graph).add(bind);
		}else {
			ArrayList<Bindings> bindList = new ArrayList<Bindings>();
			bindList.add(bind);
			ArrayList<String> vars = new ArrayList<String>();
			vars.add("s");
			vars.add("p");
			vars.add("o");
			added.put(graph,new BindingsResults(vars, bindList));	
		}
	}
	private void addToRemoved(String graph, Bindings bind) {	
		if(removed.containsKey(graph)) {
			removed.get(graph).add(bind);
		}else {
			ArrayList<Bindings> bindList = new ArrayList<Bindings>();
			bindList.add(bind);
			ArrayList<String> vars = new ArrayList<String>();
			vars.add("s");
			vars.add("p");
			vars.add("o");
			removed.put(graph,new BindingsResults(vars, bindList));	
		}
	}
	private String generateSelectExistsList(String values) {
		return "SELECT ?x ?i {VALUES (?g ?s ?p ?o ?i) { \n" + values+ "} BIND(EXISTS{GRAPH ?g {?s ?p ?o}} AS ?x)}";
	}
	
	private String incapsulate(String graph,Bindings bind,int index ) {
		return "(<"+graph+"><"+bind.getValue("s")+"><"+bind.getValue("p")+"><"+bind.getValue("o")+"> "+index+")\n";
	}
	

	
	public BindingsResults getBindings(String query, SparqlObj sparql, EndPoint ep)  {
		
		
		SparqlObj askSparql= sparql;
		askSparql.setSparql(query);
		SparqlRequest askquery = new SparqlRequest(askSparql,ep);
		return ((QueryResponse)askquery.execute()).getBindingsResults();
		
	}
	
	
	public HashMap<String,BindingsResults> getReorganizedBindingsForAdded(SparqlObj sparql, EndPoint ep) throws SEPABindingsException  {
	
		return added;		
		
	}
	
	
	public HashMap<String,BindingsResults> getReorganizedBindingsForRemoved(SparqlObj sparql, EndPoint ep) throws SEPABindingsException  {
		
		return removed;		
		
	}

	public HashMap<String,BindingsResults> getReorganizedBindingsForAdded() throws SEPABindingsException  {
		
		return added;		
		
	}
	
	
	public HashMap<String,BindingsResults> getReorganizedBindingsForRemoved() throws SEPABindingsException  {
		
		return removed;		
		
	}

	public boolean needAskSelectForAdded() {
		return   added!=null &&   added.size()>0;
	}


	public boolean needAskSelectForRemoved() {
		return   removed!=null &&   removed.size()>0;
	}

	
}

class BindingsWrapper{
	private Bindings bind;
	private int listNumer;
	private boolean added;
	private String graph;
	
	public BindingsWrapper(Bindings bind, int listNumer, boolean added,String graph) {
		super();
		this.bind = bind;
		this.listNumer = listNumer;
		this.added = added;
		this.graph=graph;
	}
	public Bindings getBind() {
		return bind;
	}
	public void setBind(Bindings bind) {
		this.bind = bind;
	}
	public int getListNumer() {
		return listNumer;
	}
	public void setListNumer(int listNumer) {
		this.listNumer = listNumer;
	}
	public boolean isAdded() {
		return added;
	}
	public void setAdded(boolean added) {
		this.added = added;
	}
	public String getGraph() {
		return graph;
	}
	public void setGraph(String graph) {
		this.graph = graph;
	}
	
	
}
