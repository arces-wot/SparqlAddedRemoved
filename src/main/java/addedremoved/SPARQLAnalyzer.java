package addedremoved;

import org.apache.jena.atlas.lib.Sink;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.algebra.*;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.lang.UpdateParserFactory;
import org.apache.jena.sparql.lang.arq.ParseException;
import org.apache.jena.sparql.modify.request.*;
import org.apache.jena.sparql.syntax.*;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.UpdateConstruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SPARQLAnalyzer {

	String test = null;

	public void setString(String s) {
		test = s;
	}

	class MyTransform extends TransformCopy {
		@Override
		public Op transform(OpBGP opBGP) {
			// create a new construct query
			Query q = QueryFactory.make();
			q.setQueryConstructType();

			// parse the bgp
			BasicPattern b = opBGP.getPattern();
			Iterator<Triple> opIterator = b.iterator();
			Template ttt = new Template(b);
			q.setConstructTemplate(ttt);
			ElementGroup body = new ElementGroup();
			ElementUnion union = new ElementUnion();

			while (opIterator.hasNext()) {
				Triple bb = opIterator.next();

				// for the query
				ElementTriplesBlock block = new ElementTriplesBlock(); // Make a BGP
				block.addTriple(bb);
				body.addElement(block);
				logger.debug(bb.toString());

				// union
				union.addElement(block);

			}

			q.setQueryPattern(body);
			q.setQueryPattern(union);

			setString(q.toString());
			logger.debug(q.toString());

			return opBGP;
		}
	}

	class ToConstructUpdateVisitor extends UpdateVisitorBase {
		private UpdateConstruct result = new UpdateConstruct("", "");

		@Override
		public void visit(UpdateDataInsert updateDataInsert) {
			//---------------------------------------------------------------REWORKED
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
			ConstructGenerator cg = new ConstructGenerator(updateDataInsert.getQuads());			
			String insertString = cg.getConstruct();
			//OLD CODE------------------INIZIO
			//			Query insertQuery = createBaseConstruct(new QuadAcc(updateDataInsert.getQuads()));
			//			String insertString = insertQuery.isUnknownType() ? "" : insertQuery.serialize() + "WHERE{}";
			//OLD CODE------------------FINE
			result = new UpdateConstruct("", insertString);
			//System.out.println("1");
		}

		@Override
		public void visit(UpdateDataDelete updateDataDelete) {
			Query deleteQuery = createBaseConstruct(new QuadAcc(updateDataDelete.getQuads()));
			String deleteString = deleteQuery.isUnknownType() ? "" : deleteQuery.serialize() + "WHERE{}";
			result = new UpdateConstruct(deleteString, "");
//			if(deleteQuery.getGraphURIs().size()>0) {
//				result.setRemovedGraph(deleteQuery.getGraphURIs().get(0));
//				//----------------------------------STESSO GRAFO DI PRIMA O GRAFI DIVERSI??
//			}
//
			System.out.println("2");
		}

		@Override
		public void visit(UpdateDeleteWhere updateDeleteWhere) {
			Query updateDeleteQuery = createBaseConstruct(new QuadAcc(updateDeleteWhere.getQuads()));
			if (!updateDeleteQuery.isUnknownType()) {
				ElementGroup where = new ElementGroup();
//				String graph = null;
				for (Quad q : updateDeleteWhere.getQuads()) {
					where.addTriplePattern(q.asTriple());
					//----------------------------------STESSO GRAFO DI PRIMA O GRAFI DIVERSI??
//					graph=q.getGraph().getURI();
				}
				updateDeleteQuery.setQueryPattern(where);
				result = new UpdateConstruct(updateDeleteQuery.serialize(), "");
//				result.setRemovedGraph(graph);
			}

			System.out.println("3");
		}

		@Override
		public void visit(UpdateModify updateModify) {
			String insertString = "";
			String deleteString = "";
//			String insertGraph =null;
//			String deleteGraph =null;
			if (updateModify.hasDeleteClause() && !updateModify.getDeleteAcc().getQuads().isEmpty()) {
				Template constructDelete = new Template(updateModify.getDeleteAcc());
				Query constructQueryDelete = new Query();
				constructQueryDelete.setQueryConstructType();
				constructQueryDelete.setConstructTemplate(constructDelete);
				constructQueryDelete.setQueryPattern(updateModify.getWherePattern());
				deleteString = constructQueryDelete.toString();
//				if(updateModify.getDeleteAcc().getGraph()!=null) {
//					deleteGraph=updateModify.getDeleteAcc().getGraph().getURI();
//				}
			}

			if (updateModify.hasInsertClause() && !updateModify.getInsertAcc().getQuads().isEmpty()) {
				Template constructInsert = new Template(updateModify.getInsertAcc());
				Query constructQueryInsert = new Query();
				constructQueryInsert.setQueryConstructType();
				constructQueryInsert.setConstructTemplate(constructInsert);
				constructQueryInsert.setQueryPattern(updateModify.getWherePattern());
				insertString = constructQueryInsert.serialize();
//				if(updateModify.getInsertAcc().getGraph()!=null) {
//					insertGraph=updateModify.getInsertAcc().getGraph().getURI();
//				}
			}
			result = new UpdateConstruct(deleteString, insertString);
//			if(updateModify.getWithIRI()!=null && insertGraph==null) {
//				result.setAddedGraph(updateModify.getWithIRI().getURI());
//			}else {
//				result.setAddedGraph(insertGraph);
//			}
//			if(updateModify.getWithIRI()!=null && deleteGraph==null) {
//				result.setRemovedGraph(updateModify.getWithIRI().getURI());
//			}else {
//				result.setRemovedGraph(deleteGraph);
//			}
//
			System.out.println("4");
		}

		@Override
		public void visit(UpdateClear update) {
			String deleteConstruct = "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" + update.getGraph().getURI()
					+ "> { ?s ?p ?o } . }";
			result = new UpdateConstruct(deleteConstruct, "");
//			result.setRemovedGraph(update.getGraph().getURI());
//
			System.out.println("5");
		}

		@Override
		public void visit(UpdateDrop update) {
			String deleteConstruct = "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" + update.getGraph().getURI()
					+ "> { ?s ?p ?o } . }";
			result = new UpdateConstruct(deleteConstruct, "");
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
			result = new UpdateConstruct(deleteConstruct, insertConstruct);
//			result.setGraph(update.getDest().getGraph().getURI());
//
			System.out.println("7");
		}

		@Override
		public void visit(UpdateAdd update) {
			String insertConstruct = "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" + update.getDest().getGraph().getURI()
					+ "> { ?s ?p ?o } . }";
			result = new UpdateConstruct("", insertConstruct);
//			result.setAddedGraph(update.getDest().getGraph().getURI());
//
			System.out.println("8");
		}

		// TODO: Move

		public UpdateConstruct getResult() {
			return result;
		}

		private Query createBaseConstruct(QuadAcc quads) {
			Query result = new Query();
			if (!quads.getQuads().isEmpty()) {
				Template construct = new Template(quads);
				result = new Query();
				result.setQueryConstructType();
				result.setConstructTemplate(construct);
			}
			return result;
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

	public UpdateConstruct getConstruct() {
		UpdateRequest updates = UpdateFactory.create(sparqlText);
		String graph= GraphAnalyzer.getGraphURIs(sparqlText).iterator().next();
		//System.out.println("Graph: "+graph);
		for (Update up : updates) {			
			ToConstructUpdateVisitor updateVisitor = new ToConstructUpdateVisitor();
			up.visit(updateVisitor);
			UpdateConstruct c=updateVisitor.getResult();
			c.setGraph(graph);
			return c;
		}
		throw new IllegalArgumentException("No valid operation found");
	}

	// Construct Generator
	String getConstructFromQuery() throws ParseException {

		// This method allows to derive the CONSTRUCT query
		// from the SPARQL SUBSCRIPTION

		// get the algebra from the query

		Query qqq = QueryFactory.create(sparqlText, Syntax.syntaxSPARQL);
		Op op = Algebra.compile(qqq);

		// get the algebra version of the construct query and
		// convert it back to query
		Transform transform = new MyTransform();
		op = Transformer.transform(transform, op);
		Query q = OpAsQuery.asQuery(op);
		// return
		return test;

	}

}