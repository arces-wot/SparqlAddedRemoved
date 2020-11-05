package addedremoved;

import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.modify.request.*;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SPARQLAnalyzer {


	class ToConstructUpdateVisitor extends UpdateVisitorBase {
		private ArrayList<UpdateConstruct> results = new ArrayList<UpdateConstruct>();

		@Override
		public void visit(UpdateDataInsert updateDataInsert) {//----------------REWORKED
			//Esempio di update che passa qui: Update del test MT1
			//CONSTRUCT costruita tramite jena:
			/*
			  CONSTRUCT{ 
					    GRAPH <http://lumb/for.sepa.test/workspace/defaultgraph> 
					      { <http://www.unibo.it/Student0> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#memberOf> <http://www.unibo.it> .}
					    GRAPH <http://lumb/for.sepa.test/workspace/defaultgraph> 
					      { <http://www.unibo.it/Student1> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#memberOf> <http://www.unibo.it> .}
					  }WHERE{}
			 */
			//CONTRUCT ACCETTATA DA SPARQL 1.1:
			/*
			  CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <http://lumb/for.sepa.test/workspace/defaultgraph> {
					  <http://www.unibo.it/Student0> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#memberOf> <http://www.unibo.it> .
					  <http://www.unibo.it/Student1> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#memberOf> <http://www.unibo.it> .
					 } 			 
			 */
			
			//OLD CODE------------------INIZIO
			//			Query insertQuery = createBaseConstruct(new QuadAcc(updateDataInsert.getQuads()));
			//			String insertString = insertQuery.isUnknownType() ? "" : insertQuery.serialize() + "WHERE{}";
			//OLD CODE------------------FINE
			ConstructGenerator cg = new ConstructGenerator(updateDataInsert.getQuads());	
			HashMap<String,ArrayList<Triple>> insertTriples =cg.getAllTriple();
			for (String graph :insertTriples.keySet()) {
				results.add(new UpdateConstruct(null,insertTriples.get(graph),graph));
			}
			//System.out.println("1");
		}

		@Override
		public void visit(UpdateDataDelete updateDataDelete) {//----------------REWORKED
			//OLD CODE------------------INIZIO
//			Query deleteQuery = createBaseConstruct(new QuadAcc(updateDataDelete.getQuads()));
//			String deleteString = deleteQuery.isUnknownType() ? "" : deleteQuery.serialize() + "WHERE{}";
//			results.add(new UpdateConstruct(deleteString, ""));
			//OLD CODE------------------FINE
			ConstructGenerator cg = new ConstructGenerator(updateDataDelete.getQuads());	
			HashMap<String,ArrayList<Triple>> deleteTriples =cg.getAllTriple();
			for (String graph :deleteTriples.keySet()) {
				results.add(new UpdateConstruct(deleteTriples.get(graph),null,graph));
			}
			//System.out.println("2");
		}

		@Override
		public void visit(UpdateDeleteWhere updateDeleteWhere) {//----------------REWORKED
			//Esempio di update che passa qui: Update del test MT2
			//esempio CONSTRUCT JENA: 
			/*
					 CONSTRUCT{ 
					   GRAPH <http://lumb/for.sepa.test/workspace/defaultgraph> 
					      { ?s <http://swat.cse.lehigh.edu/onto/univ-bench.owl#memberOf> <http://www.unibo.it> .}
					 }WHERE{ ?s  <http://swat.cse.lehigh.edu/onto/univ-bench.owl#memberOf>  <http://www.unibo.it> }
			 */
			//OLD CODE
//			Query updateDeleteQuery = createBaseConstruct(new QuadAcc(updateDeleteWhere.getQuads()));
//			if (!updateDeleteQuery.isUnknownType()) {
//				ElementGroup where = new ElementGroup();
//				for (Quad q : updateDeleteWhere.getQuads()) {
//					where.addTriplePattern(q.asTriple());
//				}
//				updateDeleteQuery.setQueryPattern(where);
//				result = new UpdateConstruct(updateDeleteQuery.serialize(), "");
//			}

			ConstructGenerator cg = new ConstructGenerator(updateDeleteWhere.getQuads());	
			HashMap<String,String> deleteStrings =cg.getConstructsWithGraphs(true);
			for (String graph : deleteStrings.keySet()) {
				results.add(new UpdateConstruct(deleteStrings.get(graph), "",graph,""));
			}
			
			//System.out.println("3");
		}

		@Override
		public void visit(UpdateModify updateModify) {//----------------REWORKED

			//OLD CODE:
			/*
			String insertString = "";
			String deleteString = "";
			if (updateModify.hasDeleteClause() && !updateModify.getDeleteAcc().getQuads().isEmpty()) {
				Template constructDelete = new Template(updateModify.getDeleteAcc());
				Query constructQueryDelete = new Query();
				constructQueryDelete.setQueryConstructType();
				constructQueryDelete.setConstructTemplate(constructDelete);
				constructQueryDelete.setQueryPattern(updateModify.getWherePattern());
				deleteString = constructQueryDelete.toString();
			}

			if (updateModify.hasInsertClause() && !updateModify.getInsertAcc().getQuads().isEmpty()) {
				Template constructInsert = new Template(updateModify.getInsertAcc());
				Query constructQueryInsert = new Query();
				constructQueryInsert.setQueryConstructType();
				constructQueryInsert.setConstructTemplate(constructInsert);
				constructQueryInsert.setQueryPattern(updateModify.getWherePattern());
				insertString = constructQueryInsert.serialize();
			}
			*/
			
			
			HashMap<String,String> insertStrings=null;
			HashMap<String,String> deleteStrings=null;
			
			if (updateModify.hasDeleteClause() && !updateModify.getDeleteAcc().getQuads().isEmpty()) {				
				ConstructGenerator cg = new ConstructGenerator(updateModify.getDeleteAcc().getQuads());
				
				deleteStrings=cg.getConstructsWithGraphs(updateModify.getWherePattern().toString());				
			}

			if (updateModify.hasInsertClause() && !updateModify.getInsertAcc().getQuads().isEmpty()) {
				ConstructGenerator cg = new ConstructGenerator(updateModify.getInsertAcc().getQuads());	
				insertStrings=cg.getConstructsWithGraphs(updateModify.getWherePattern().toString());		
			}
			
			if(insertStrings!=null && deleteStrings!=null) {
				Set<String> graphs = new HashSet<String>(insertStrings.keySet());
				graphs.addAll(deleteStrings.keySet());
				for (String graph : graphs) {
					String deleteString="";
					String insertString="";
					if(insertStrings.containsKey(graph)) {
						insertString=insertStrings.get(graph);
					}
					if(deleteStrings.containsKey(graph)) {
						deleteString=deleteStrings.get(graph);
					}
					results.add(new UpdateConstruct(deleteString, insertString,graph));
				}
			}else if(insertStrings==null) {
				for (String graph : deleteStrings.keySet()) {
					results.add(new UpdateConstruct(deleteStrings.get(graph), "",graph));
				}
			}else {//deleteStrings==null
				for (String graph : insertStrings.keySet()) {
					results.add(new UpdateConstruct("", insertStrings.get(graph),graph));
				}
			}
			
			//System.out.println("4");
		}

		@Override
		public void visit(UpdateClear update) {
			String deleteConstruct = "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" + update.getGraph().getURI()
					+ "> { ?s ?p ?o } . }";
			results.add(new UpdateConstruct(deleteConstruct, "",update.getGraph().getURI(),""));
//			result.setRemovedGraph(update.getGraph().getURI());
//
			System.out.println("5");
		}

		@Override
		public void visit(UpdateDrop update) {
			String deleteConstruct = "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" + update.getGraph().getURI()
					+ "> { ?s ?p ?o } . }";
			results.add(new UpdateConstruct(deleteConstruct, "",update.getGraph().getURI(),""));
//			result.setRemovedGraph(update.getGraph().getURI());
//
			System.out.println("6");
		}

		@Override
		public void visit(UpdateCopy update) {
			String deleteConstruct = "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" + update.getDest().getGraph().getURI()
					+ "> { ?s ?p ?o } . }";
			String insertConstruct = "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" + update.getSrc().getGraph().getURI()
					+ "> { ?s ?p ?o } . }";
			results.add(new UpdateConstruct(deleteConstruct, insertConstruct,update.getDest().getGraph().getURI(),update.getSrc().getGraph().getURI()));
//			result.setGraph(update.getDest().getGraph().getURI());
//
			System.out.println("7");
		}

		@Override
		public void visit(UpdateAdd update) {
			String insertConstruct = "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" + update.getDest().getGraph().getURI()
					+ "> { ?s ?p ?o } . }";
			results.add(new UpdateConstruct("", insertConstruct,"",update.getDest().getGraph().getURI()));
//			result.setAddedGraph(update.getDest().getGraph().getURI());
//
			System.out.println("8");
		}

		// TODO: Move

		public ArrayList<UpdateConstruct> getResult() {
			return results;
		}



	}

	// attributes
	private String sparqlText;
	private final static Logger logger = LogManager.getLogger("SPARQLAnalyzer");

	// Constructor
	public SPARQLAnalyzer(String request) {
		// store the query text
		sparqlText = request;
	}

	public ArrayList<UpdateConstruct> getConstructs() {
		//System.out.println("sparqlText:\n"+sparqlText);
		UpdateRequest updates = UpdateFactory.create(sparqlText);
		for (Update up : updates) {			
			ToConstructUpdateVisitor updateVisitor = new ToConstructUpdateVisitor();
			up.visit(updateVisitor);
			return updateVisitor.getResult();
		}
		throw new IllegalArgumentException("No valid operation found");
	}



}