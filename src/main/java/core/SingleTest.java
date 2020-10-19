package core;

import java.util.ArrayList;

import addedremoved.AddedRemovedGenerator;
import connector.SparqlRequest;
import it.unibo.arces.wot.sepa.commons.response.QueryResponse;
import it.unibo.arces.wot.sepa.commons.response.Response;
import model.TestMetric;
import model.TestResult;
import model.UpdateConstruct;

public class SingleTest implements ITest {

	
		private SparqlRequest preparationInsert=null;
		private SparqlRequest query=null;
		private SparqlRequest update=null;
		private SparqlRequest rollback=null;
		private SparqlRequest rollbackPreparation=null;
	
		private boolean excAskTest;
		/*
		 * excAskTest need to be false if:
		 * 1) The update insert or delete triples aren't catch at all by the query
		 * 2) The query select pattern is not equals to that triples
		 */

		

		public SingleTest(SparqlRequest query, SparqlRequest update, SparqlRequest rollback) {
			super();
			this.query = query;
			this.update = update;
			this.rollback = rollback;
			this.excAskTest=false;
		}

		public SingleTest(SparqlRequest query, SparqlRequest update, SparqlRequest rollback,boolean excAskTest) {
			this(query,update,rollback);
			this.excAskTest=excAskTest;
		}
		
		public TestResult execute() {
			ArrayList<TestMetric> phases = new ArrayList<TestMetric> ();
			SparqlRequest deleteUpdate=null;
			SparqlRequest insertUpdate=null;
			Inspector inspector = new Inspector();
			//------------------------------------------------------------Phase 1
			//-----------Prepare
			//System.out.println("-->"+preparationInsert.getSparql().getSparqlString());
			TestMetric phase1 = new TestMetric("Preparation");	
			if(preparationInsert!=null) {
				phase1.start();
				boolean preparationFail = preparationInsert.execute().isError();
				phase1.stop();
				phase1.setError(preparationFail);
			}	
			phases.add(phase1);
			
			//------------------------------------------------------------Phase 2
			//-----------Generation, CONSTRUCT and ASKs
			TestMetric phase2 = new TestMetric("Added removed extraction and generation of updates (insert and delete)");		
			
			phase2.start();
			UpdateConstruct constructs = AddedRemovedGenerator.getAddedRemovedFrom(update.clone(),phases);
			boolean  pahes2Err = false;
			if(constructs.needDelete()) {
				try {
					deleteUpdate =AddedRemovedGenerator.generateDeleteUpdate(update.clone(),constructs);				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					pahes2Err=true;
				}
			}
			if(constructs.needInsert()) {
				try {
					insertUpdate =AddedRemovedGenerator.generateInsertUpdate(update.clone(),constructs);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					pahes2Err=true;
				}
			}
			phase2.stop(pahes2Err);
			if(constructs==null || (deleteUpdate==null && insertUpdate==null) ){
				System.out.println("Error: construct or insert-delete generatio fail" );
				phase2.setError(true);
			}
			phases.add(phase2);
		
			
		    inspector.setAskForDelete(constructs.getRemoved());
		    inspector.setAskForInsert(constructs.getAdded());
			
			//------------------------------------------------------------Phase 3
			//-----------QUERY
			TestMetric phase3 = new TestMetric("Added removed extraction and generation of updates (insert and delete)");		
			phase3.start();
			Response pre_ris_query = query.execute();
			phase3.stop(pre_ris_query.isError());
			phases.add(phase3);			
			inspector.setQuery(((QueryResponse)pre_ris_query).getBindingsResults());
			
			//------------------------------------------------------------Phase 4
			//-----------UPDATE
			TestMetric phase4 = new TestMetric("Execution normal update");
			phase4.start();
			Response ris_update =  update.execute();
			phase4.stop(ris_update.isError());
			phases.add(phase4);
			
			//------------------------------------------------------------Phase 5
			//-----------QUERY
			TestMetric phase5 = new TestMetric("Execution Query after normal update");
			phase5.start();
			Response ris_Query =query.execute();
			phase5.stop(ris_Query.isError());
			phases.add(phase5);			
			inspector.setQueryAfterNormalUpdate(((QueryResponse)ris_Query).getBindingsResults());
		
			//------------------------------------------------------------Phase 6
			//-----------ROOLBACK
			TestMetric phase6 = new TestMetric("Execution RollBack Update");
			phase6.start();
			Response ris_Rollback =rollback.execute();
			phase6.stop(ris_Rollback.isError());
			phases.add(phase6);			

			//------------------------------------------------------------Phase 7
			//-----------INSERT + DELETE 			
			TestMetric phase7 = new TestMetric("Execution insert and delete");
			Response ris_insert =null;
			Response ris_delete =null;
			
			phase7.start();
			boolean insertDellErro = false;
			if(deleteUpdate!=null) {
				ris_delete =deleteUpdate.execute();
				if(ris_delete.isError()) {
					insertDellErro=true;
				}
			}
			if(insertUpdate!=null) {
				ris_insert =insertUpdate.execute();
				if(ris_insert.isError()) {
					insertDellErro=true;
				}
			}
			phase7.stop(insertDellErro);
			phases.add(phase7);
			
			//------------------------------------------------------------Phase 8
			//-----------QUERY
			TestMetric phase8 = new TestMetric("Execution Query after insert-delete");	
			phase8.start();
			Response ris_Query_2 = query.execute();
			phase8.stop(ris_Query_2.isError());
			phases.add(phase8);			
			inspector.setQueryAfterInsertDell(((QueryResponse)ris_Query_2).getBindingsResults());
			
			//------------------------------------------------------------Phase 9
			//-----------ROOLBACK
			TestMetric phase9 = new TestMetric("Re-Execution RollBack");			
			phase9.start();
			ris_Rollback =rollback.execute();
			phase9.stop(ris_Rollback.isError());
			phases.add(phase9);
			
			//------------------------------------------------------------Phase 10
			//-----------ROOLBACK PREPARATION			
			TestMetric phase10 = new TestMetric("RollBack preparation");	
			if(preparationInsert!=null && rollbackPreparation!=null) {
				phase10.start();
				ris_Rollback =rollbackPreparation.execute();
				phase10.stop(ris_Rollback.isError());
				phases.add(phase10);
			}
		
			
			
			//------------------------------------Bulding result
    		
			return inspector.getResult(phases, excAskTest);
		}
	
		//-----------------------------------------------------------------SETTERS and GETTERS
		
		public SparqlRequest getPreparationInsert() {
			return preparationInsert;
		}
		
		public SparqlRequest getQuery() {
			return query;
		}

		public void setQuery(SparqlRequest query) {
			this.query = query;
		}

		public SparqlRequest getUpdate() {
			return update;
		}

		public void setUpdate(SparqlRequest update) {
			this.update = update;
		}

		public SparqlRequest getRollback() {
			return rollback;
		}

		public void setRollback(SparqlRequest rollback) {
			this.rollback = rollback;
		}

		public SparqlRequest getRollbackPreparation() {
			return rollbackPreparation;
		}

		public void setPreparation(SparqlRequest preparationInsert,SparqlRequest rollbackPreparation) {
			this.rollbackPreparation = rollbackPreparation;
			this.preparationInsert = preparationInsert;
		}

		public boolean isExcAskTest() {
			return excAskTest;
		}

		public void setExcAskTest(boolean excAskTest) {
			this.excAskTest = excAskTest;
		}

	
		
}
