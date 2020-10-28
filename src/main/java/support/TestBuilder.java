package support;

import org.apache.jena.base.Sys;

import connector.SparqlRequest;
import core.test.MetaTest;
import core.test.SingleTest;
import model.TripleBase;

public class TestBuilder {

	public static SingleTest build(MetaTest meta,int triplesNumber) {
		
		SparqlRequest update = meta.getUpdate().generate(triplesNumber);
		
		SparqlRequest rollback = meta.getRollback().generate(triplesNumber);

		SparqlRequest query = meta.getQuery().generate(triplesNumber);
		
		SingleTest ris = new SingleTest(query,update,rollback,meta.isExcAskTest());		
		ris.setTestTripleCount(triplesNumber);
		if(meta.isNeedPreparation()){
			int prapreTripleNumber =triplesNumber*meta.getPreparationPercentage()/100;
			ris.setPreInseredTestTirpleCount(prapreTripleNumber);
			if(prapreTripleNumber>0) {

				SparqlRequest prepareInsert = meta.getPreparationInsert().generate(prapreTripleNumber);
				SparqlRequest prepareDelete = meta.getRollbackPreparation().generate(prapreTripleNumber);
			
				ris.setPreparation(prepareInsert, prepareDelete);
				return ris;
			}			
		
		}else {
			ris.setPreInseredTestTirpleCount(0);
		}
		return ris;
	
	}
	

	
}
