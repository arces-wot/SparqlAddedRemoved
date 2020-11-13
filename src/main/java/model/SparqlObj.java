package model;
import java.util.HashSet;
import java.util.Set;

public class SparqlObj implements Cloneable {
	private String sparql;
	private Set<String> default_graph_uri;
	private Set<String> named_graph_uri;
	
	public SparqlObj( String sparql) {
		super();
		this.sparql = sparql;
		this.default_graph_uri =new HashSet<String>();
		this.named_graph_uri = new HashSet<String>();
	}
	
	
	public SparqlObj(String sparql, Set<String> default_graph_uri, Set<String> named_graph_uri) {
		super();
		this.sparql = sparql;
		this.default_graph_uri = default_graph_uri;
		this.named_graph_uri = named_graph_uri;
	}
	
	

	public String getSparqlString() {
		return sparql;
	}
	public void setSparql(String sparql) {
		this.sparql = sparql;
	}
	public Set<String> getDefault_graph_uri() {
		return default_graph_uri;
	}
	public void setDefault_graph_uri(Set<String> default_graph_uri) {
		this.default_graph_uri = default_graph_uri;
	}
	public Set<String> getNamed_graph_uri() {
		return named_graph_uri;
	}
	public void setNamed_graph_uri(Set<String> named_graph_uri) {
		this.named_graph_uri = named_graph_uri;
	}
	
	public SparqlObj clone()throws CloneNotSupportedException{  
		return (SparqlObj)super.clone();  
	}  
		  

}
