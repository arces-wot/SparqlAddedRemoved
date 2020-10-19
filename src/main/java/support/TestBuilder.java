package support;

import org.apache.jena.base.Sys;

import connector.SparqlRequest;
import core.MetaTest;
import core.SingleTest;
import model.TripleBase;

public class TestBuilder {
	private static String builderBindInsert = "_?_I_TRIPLES_?_";
	private static String builderBindDelete = "_?_D_TRIPLES_?_";
	public static String getBuilderBindInsert() {
		return builderBindInsert;
	}	
	public static String getBuilderBindDelete() {
		return builderBindDelete;
	}
	public static SingleTest build(MetaTest meta,int triplesNumber) {
		
		SparqlRequest update = meta.getTest().getUpdate().clone();
		TripleBase tb = meta.getTriples();
		update.setSparqlStr(insertTripleToSparql(
				update.getSparql().getSparqlString(),
				tb,
				triplesNumber
		));
		
		SparqlRequest rollback = meta.getTest().getRollback().clone();
		
		rollback.setSparqlStr(insertTripleToSparql(
				rollback.getSparql().getSparqlString(),
				tb,
				triplesNumber
		));
		
		SingleTest ris = new SingleTest(meta.getTest().getQuery(),update,rollback);		
		
		if(meta.isNeedPreparation()){
			SparqlRequest prepareInsert = meta.getTest().getPreparationInsert().clone();
			SparqlRequest prepareDelete = meta.getTest().getRollbackPreparation().clone();
			int prapreTripleNumber =triplesNumber*meta.getPreparationPercentage()/100;
			if(prapreTripleNumber>0) {
				prepareInsert.setSparqlStr(insertTripleToSparql(
						prepareInsert.getSparql().getSparqlString(),
						tb,
						prapreTripleNumber
				));			
				
				prepareDelete.setSparqlStr(insertTripleToSparql(
						prepareDelete.getSparql().getSparqlString(),
						tb,
						prapreTripleNumber
				));
				ris.setPreparation(prepareInsert, prepareDelete);
				return ris;
			}			
		
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
			ris=ris.replace(builderBindDelete, triples);
		}
		
		
		if(needInsertInjeption) {
			String triples = "";
			for(int x =0;x<number;x++) {
				triples+=triple.getNextTriple()+". \n";
			}
			ris=ris.replace(builderBindInsert, triples);
		}
		return ris;
	}
	
	
}
