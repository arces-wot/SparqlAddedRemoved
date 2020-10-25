package addedremoved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.TriplePattern;
import org.apache.jena.shacl.engine.constraint.SparqlComponent;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;

public class ConstructGenerator {
	
	private HashMap<String,ArrayList<Triple>> allTriple = new  HashMap<String,ArrayList<Triple>>();

	
	public ConstructGenerator(List<Quad> quadList) {
		for(Quad q :quadList) {
			this.add(q.getGraph().getURI(),q.asTriple());
		}
	}
	
	public void add(String graph,Triple t) {
		if(allTriple.containsKey(graph)) {
			allTriple.get(graph).add(t);
		}else {
			ArrayList<Triple> new_List = new ArrayList<Triple>();
			new_List.add(t);
			allTriple.put(graph, new_List);
			
		}
	}
	
	
	
	public ArrayList<String> getConstructs(boolean strict) {
		ArrayList<String> constructs = new ArrayList<String>();
		for (String graph : allTriple.keySet()) {
			constructs.add(getConstruct(graph,allTriple.get(graph),strict));
		}
		return constructs;
	}
	
	public HashMap<String,String> getConstructsWithGraphs(boolean strict) {
		HashMap<String,String> constructs = new HashMap<String,String>();
		for (String graph : allTriple.keySet()) {
			constructs.put(graph,getConstruct(graph,allTriple.get(graph),strict));
		}
		return constructs;
	}
	
	
	
	public String getConstruct(String graph,ArrayList<Triple> triples,boolean strict) {
		
		String sparql = "CONSTRUCT ";
		ElementTriplesBlock list = new ElementTriplesBlock(); //Solution1 
		String stringList = "";
		for(Triple triple :triples) {
			//stringList+= new TriplePattern(triple).toString() + ".\n"; //Solution2
			list.addTriple(triple);//Solution1 
		}	
		stringList="{"+list.toString()+"}";//Solution1 

		String where = " WHERE { \n";
		if(strict){
			where+="GRAPH <"+ graph + "> "+stringList +"\n";
		}else{
			where+="GRAPH <"+ graph + "> {?s ?p ?o}\n";
		}
		sparql+=stringList+ where +"}";

		//System.out.println("construct:\n"+sparql);
		return sparql;
	}

	public HashMap<String, ArrayList<Triple>> getAllTriple() {
		return allTriple;
	}
	

	
	
	
}
