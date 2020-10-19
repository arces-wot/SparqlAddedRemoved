package support;

import connector.SparqlRequest;
import core.MetaTest;
import core.SingleTest;
import model.TripleBase;

public class TestBuilder {
	private static String builderBindInsert = "_?_INSERT_TRIPLES_?_";
	private static String builderBindDelete = "_?_DELETE_TRIPLES_?_";
	public static String getBuilderBindInsert() {
		return builderBindInsert;
	}	
	public static String getBuilderBindDelete() {
		return builderBindDelete;
	}
	public static SingleTest build(MetaTest meta,int triplesNumber) {
		
		SparqlRequest update = meta.getTest().getUpdate().clone();
		
		update.setSparql(insertTripleToSparql(
				update.getSparql().getSparqlString(),
				meta.getUpdate_roolbackTriples(),
				triplesNumber
		));
		
		SparqlRequest roolback = meta.getTest().getRoolback().clone();
		
		roolback.setSparql(insertTripleToSparql(
				roolback.getSparql().getSparqlString(),
				meta.getUpdate_roolbackTriples(),
				triplesNumber
		));
		
		SingleTest ris = new SingleTest(meta.getTest().getQuery(),update,roolback);		
		
		if(meta.isNeedPreparation()){
			
			SparqlRequest prepareInsert = meta.getTest().getPreparationInsert().clone();
			SparqlRequest prepareDelete = meta.getTest().getRoolbackPreparation().clone();
			int percentage =meta.getPreparationPercentage();
			
			prepareInsert.setSparql(insertTripleToSparql(
					prepareInsert.getSparql().getSparqlString(),
					meta.getUpdate_roolbackTriples(),
					(triplesNumber*percentage/100)
			));
			
			prepareDelete.setSparql(insertTripleToSparql(
					prepareDelete.getSparql().getSparqlString(),
					meta.getUpdate_roolbackTriples(),
					(triplesNumber*percentage/100)
			));
			
			ris.setPreparation(prepareInsert, prepareDelete);
			return ris;
		
		}
		
		return ris;
	
	}
	
	public static String insertTripleToSparql(String sparql, TripleBase triple, int number) {
		
		boolean needInsertInjeption = sparql.contains(builderBindInsert);
		boolean needDeleteInjeption = sparql.contains(builderBindDelete);		
		String ris = sparql;
		triple.reset();
		
		
		//La delete DEVE stare prima della insert
		//dato che il numero aggiuntivo per la generazione delle triple non viene resettato
		//e in caso di delete con preparazione, tali triple devono fare mach
		if(needDeleteInjeption) {
			String triples = "";
			for(int x =0;x<number;x++) {
				triples+=triple.getNextTriple()+". \n";
			}
			ris.replace(builderBindDelete, triples);
		}
		
		
		if(needInsertInjeption) {
			String triples = "";
			for(int x =0;x<number;x++) {
				triples+=triple.getNextTriple()+". \n";
			}
			ris.replace(builderBindInsert, triples);
		}
		
	
		return ris;
	}
	
	
}
