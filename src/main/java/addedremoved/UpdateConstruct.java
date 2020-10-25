package addedremoved;

import java.util.ArrayList;

import org.apache.jena.graph.Triple;

import com.google.gson.JsonObject;

import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTerm;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermBNode;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermLiteral;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermURI;

public class UpdateConstruct {

    private String deleteConstruct;
    private String insertConstruct;
    private String prefix;
    private boolean skipConstruct=false;
    
	private BindingsResults added;
	private BindingsResults removed;

	private String  addedGraph=null;
	private String  removedGraph=null;

	private Bindings convertTripleToBindings(Triple t) {
		Bindings temp = new Bindings();
		if(t.getSubject().isLiteral()){
			temp.addBinding("s", new RDFTermLiteral(t.getSubject().toString()));
		}else if(t.getSubject().isURI()) {
			temp.addBinding("s", new RDFTermURI(t.getSubject().getURI()));			
		}else if(t.getSubject().isBlank()) {
			temp.addBinding("s", new RDFTermBNode(t.getSubject().toString()));		
		}else {
			System.out.println("Warning, cannot convert Subject of Triple to Bindings, for triple: "+t.toString());
		}
		if(t.getPredicate().isLiteral()){
			temp.addBinding("p", new RDFTermLiteral(t.getPredicate().toString()));
		}else if(t.getPredicate().isURI()) {
			temp.addBinding("p", new RDFTermURI(t.getPredicate().getURI()));			
		}else if(t.getPredicate().isBlank()) {
			temp.addBinding("p", new RDFTermBNode(t.getPredicate().toString()));		
		}else {
			System.out.println("Warning, cannot convert Predicate of Triple to Bindings, for triple: "+t.toString());
		}
		if(t.getObject().isLiteral()){
			temp.addBinding("o", new RDFTermLiteral(t.getObject().toString()));
		}else if(t.getObject().isURI()) {
			temp.addBinding("o", new RDFTermURI(t.getObject().getURI()));			
		}else if(t.getObject().isBlank()) {
			temp.addBinding("o", new RDFTermBNode(t.getObject().toString()));		
		}else {
			System.out.println("Warning, cannot convert Object of Triple to Bindings, for triple: "+t.toString());
		}
		return temp;
	}
	
	
	public UpdateConstruct(ArrayList<Triple> removed,String removedGraph, ArrayList<Triple> added, String addedGraph) {
		this.added= new BindingsResults(new JsonObject());
		if(added!=null) {
			for (Triple triple : added) {
				this.added.add(convertTripleToBindings(triple));
			}
		}
			
		this.removed= new BindingsResults(new JsonObject());
		if(removed!=null) {
			for (Triple triple : removed) {
				this.removed.add(convertTripleToBindings(triple));
			}
		}
	
		this.addedGraph=addedGraph;
		this.removedGraph=removedGraph;
		this.skipConstruct=true;
	}
	
    public UpdateConstruct(String deleteConstruct, String insertConstruct ){
        if(deleteConstruct == null || insertConstruct == null){
            throw new IllegalArgumentException("Construct query cannot be null");
        }

        this.deleteConstruct = deleteConstruct;
        this.insertConstruct = insertConstruct;
    }
    public UpdateConstruct(String deleteConstruct, String insertConstruct,String deleteGraph,String insertGraph ){
        if(deleteConstruct == null || insertConstruct == null){
            throw new IllegalArgumentException("Construct query cannot be null");
        }

        this.deleteConstruct = deleteConstruct;
        this.insertConstruct = insertConstruct;
        if(deleteGraph!="") {
            this.removedGraph=deleteGraph;
        }  
        if(insertGraph!="") {
        	this.addedGraph=insertGraph;
        }
    }
	public boolean needInsert() {
		return added!=null && added.size()>0;
	}
	
	public boolean needDelete() {
		return removed!=null && removed.size()>0;
	}
	

	
	public BindingsResults getAdded() {
		return added;
	}


	public void setAdded(BindingsResults added) {
		this.added = added;
	}


	public BindingsResults getRemoved() {
		return removed;
	}


	public void setRemoved(BindingsResults removed) {
		this.removed = removed;
	}


	public String getAddedGraph() {
		return addedGraph;
	}


	public void setAddedGraph(String addedGraph) {
		this.addedGraph = fixGraphIfNeed(addedGraph);
	}


	public String getRemovedGraph() {
		return removedGraph;
	}


	public void setRemovedGraph(String removedGraph) {
		this.removedGraph = fixGraphIfNeed(removedGraph);
	}


	public void setGraph(String graph) {
		this.setAddedGraph(graph);
		this.setRemovedGraph(graph);
	}
	

	public boolean isSkipConstruct() {
		return skipConstruct;
	}
	


	public String getPrefix() {
		return prefix;
	}



	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}



	/**
     * Get delete construct string. An empty string indicates that there are no deleted
     * triples
     * @return a construct sparql query string
     */
    public String getInsertConstruct() {
        return insertConstruct;
    }

    /**
     * Get delete construct string. An empty string indicates that there are no deleted
     * triples
     * @return a construct sparql query string
     */
    public String getDeleteConstruct() {
        return deleteConstruct;
    }
    
    private String fixGraphIfNeed(String g) {
    	if(g==null) {
    		return null;
    	}
    	String ris = g;
    	if(!g.contains("<")) {
    		ris="<"+ris;
    	}
    	if(!g.contains(">")) {
    		ris+=">";
    	}
    	return ris;
    }
}
