package model;

import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;

public class UpdateConstruct {

    private final String deleteConstruct;
    private final String insertConstruct;
    private String prefix;
    
	private BindingsResults added;
	private BindingsResults removed;

	private String  addedGraph=null;
	private String  removedGraph=null;

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
