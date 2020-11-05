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
    
	private BindingsResults added=null;
	private BindingsResults removed=null;

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
	
	
	
	private UpdateConstruct(String deleteConstruct, String insertConstruct, String prefix, boolean skipConstruct,
			BindingsResults added, BindingsResults removed, String addedGraph, String removedGraph) {
		super();
		this.deleteConstruct = deleteConstruct;
		this.insertConstruct = insertConstruct;
		this.prefix = prefix;
		this.skipConstruct = skipConstruct;
		this.added = added;
		this.removed = removed;
		this.addedGraph = addedGraph;
		this.removedGraph = removedGraph;
	}//for clone



	public UpdateConstruct(ArrayList<Triple> r,ArrayList<Triple> a, String graph) {
		ArrayList<String> vars = new ArrayList<String>();
		vars.add("s");
		vars.add("p");
		vars.add("o");
		this.added= new BindingsResults(vars,  new ArrayList<Bindings>());
		if(a!=null) {
			for (Triple triple : a) {
				this.added.add(convertTripleToBindings(triple));
			}
		}
			
		this.removed=new BindingsResults(vars,  new ArrayList<Bindings>());
		if(r!=null) {
			for (Triple triple : r) {
				this.removed.add(convertTripleToBindings(triple));
			}
		}
//		System.out.println("this.removed" + this.removed.size());
		this.addedGraph=graph;
		this.removedGraph=graph;
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
        if(deleteGraph.length()>0) {
            this.removedGraph=deleteGraph;
        }  
        if(insertGraph.length()>0) {
        	this.addedGraph=insertGraph;
        }
    }
    
    public UpdateConstruct(String deleteConstruct, String insertConstruct,String graph){
        if(deleteConstruct == null || insertConstruct == null){
            throw new IllegalArgumentException("Construct query cannot be null");
        }

        this.deleteConstruct = deleteConstruct;
        this.insertConstruct = insertConstruct;
        if(graph.length()>0) {
            this.removedGraph=graph;
        	this.addedGraph=graph;
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

	
	public void removeBingingFromAddedList(Bindings bindings) {
		if(this.added !=null) {
			this.added.remove(bindings);
		}else {
			System.out.println("Warning: added BindingResult is null");
		}
	}
	
	public void removeBingingFromRemovedList(Bindings bindings) {
		if(this.removed !=null) {
			this.removed.remove(bindings);
		}else {
			System.out.println("Warning: removed BindingResult is null");
		}	
	}
	
	
	public void removeBingingFromAddedList(BindingsResults bindings) {
		if(this.added !=null) {
			for (Bindings b : bindings.getBindings()) {
				this.added.remove(b);
			}
		}else {
			System.out.println("Warning: added BindingResult is null");
		}
	}
	
	
	public void clearRemoved() {
		removed=null;
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
    public UpdateConstruct clone() {
    	return new UpdateConstruct(deleteConstruct,  insertConstruct,  prefix,  skipConstruct,
    			new BindingsResults(added.toJson()),new BindingsResults(removed.toJson()) , addedGraph,  removedGraph) ;
    }
}
