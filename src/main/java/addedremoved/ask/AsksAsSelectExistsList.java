package addedremoved.ask;

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

import addedremoved.UpdateExtractedData;
import addedremoved.epspec.EpSpecFactory;
import addedremoved.epspec.IEndPointSpecification;
import connector.SparqlRequest;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import model.EndPoint;
import model.SparqlObj;
import model.TestMetric;

public class AsksAsSelectExistsList implements IAsk{
	
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
	private ArrayList<UpdateExtractedData> ueds = new ArrayList<UpdateExtractedData> ();
	private SparqlObj sparql;
	private EndPoint endPoint;
	
	public AsksAsSelectExistsList(ArrayList<UpdateExtractedData> ueds, SparqlObj sparql, EndPoint endPoint) {
		this.ueds=ueds;
		this.sparql=sparql;
		this.endPoint=endPoint;
		this.init();
	}
	
	protected void init() {
		int orderIndex = 0;
		tripleList= new HashMap<Integer,BindingsWrapper>();	
		removed= new HashMap<String,BindingsResults>();
		added= new HashMap<String,BindingsResults>();
		String values = "";
		for (UpdateExtractedData updateConstruct : ueds) {
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
			for (Bindings bind : getBindings(generateSelect(values),sparql,endPoint).getBindings()) {
				BindingsWrapper temp =tripleList.get(Integer.parseInt(bind.getValue("i")));
				if(EpSpecFactory.getInstance().asksAsSelectExistListCompare(bind.getValue("x"))) {
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
			ArrayList<String> vars = EpSpecFactory.getInstance().vars();
			added.put(graph,new BindingsResults(vars, bindList));	
		}
	}
	
	private void addToRemoved(String graph, Bindings bind) {	
		if(removed.containsKey(graph)) {
			removed.get(graph).add(bind);
		}else {
			ArrayList<Bindings> bindList = new ArrayList<Bindings>();
			bindList.add(bind);
			ArrayList<String> vars =EpSpecFactory.getInstance().vars();
			removed.put(graph,new BindingsResults(vars, bindList));	
		}
	}
	
	protected String generateSelect(String values) {
		IEndPointSpecification eps = EpSpecFactory.getInstance();
		return "SELECT ?x ?i {VALUES (?"+eps.g()
			+" ?"+eps.s()
			+" ?"+eps.p()
			+" ?"+eps.o()
			+" ?i) { \n" + values+ "} BIND(EXISTS{GRAPH ?"+eps.g()
			+" {?"+eps.s()
			+" ?"+eps.p()
			+ "?"+eps.o()+"}} AS ?x)}";
	}
	
	protected String incapsulate(String graph,Bindings bind,int index ) {
		IEndPointSpecification eps = EpSpecFactory.getInstance();
		return "(<"+graph+"><"+bind.getValue(eps.s())
				+"><"+bind.getValue(eps.p())
				+"><"+bind.getValue(eps.o())+"> "+index+")\n";
	}
	

	
	public BindingsResults getBindings(String query, SparqlObj sparql, EndPoint ep)  {	
		SparqlObj askSparql= sparql;
		askSparql.setSparql(query);
		SparqlRequest askquery = new SparqlRequest(askSparql,ep);
		return ((QueryResponse)askquery.execute()).getBindingsResults();
		
	}
	

	public ArrayList<UpdateExtractedData> filter() throws SEPABindingsException {
		HashMap<String,BindingsResults> alredyExist_E  = this.getReorganizedBindingsForAdded();
		HashMap<String,BindingsResults> realRemoved_E = this.getReorganizedBindingsForRemoved();
		for (UpdateExtractedData constructs : ueds) {
			
			String graph = constructs.getAddedGraph();
			
			if(constructs.needInsert() && alredyExist_E.containsKey(graph) ){
				constructs.removeBingingFromAddedList(alredyExist_E.get(graph)); 

				alredyExist_E.remove(graph);
			}	
			
			graph = constructs.getRemovedGraph();
			
			if(realRemoved_E.containsKey(graph)) {
				constructs.setRemoved(realRemoved_E.get(graph));
				
				realRemoved_E.remove(graph);
			}else {
				constructs.clearRemoved();
			}
			
		}
		return ueds;
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
