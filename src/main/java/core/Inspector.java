package core;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import model.TestMetric;
import model.TestResult;

public class Inspector {

	public static boolean isVoid(BindingsResults res) {
		return res.isEmpty() && res.size()==0;
	}
	
	public static boolean areEq(BindingsResults res1,BindingsResults res2) {
		if(res1.size()==res2.size()) {
			for (Bindings bind : res1.getBindings()) {
				if(!res2.contains(bind)) {
					return false;
				}
			}
			for (Bindings bind : res2.getBindings()) {
				if(!res1.contains(bind)) {
					return false;
				}
			}
			return true;
		}else {
			return false;
		}
	}
	
	public static BindingsResults getOuterJoinA(BindingsResults A,BindingsResults B) {
		//triple che sono in A ma non sono in B
		BindingsResults ris= new BindingsResults(new BindingsResults(A.toJson()));
		for(Bindings bindings : B.getBindings()){
			ris.remove(bindings);
		}
		//System.out.println("getOuterJoinA size: "+ris.size());
		return ris;
	}
	
	
	private BindingsResults askForDelete; 	//removed
	private BindingsResults askForInsert;	//added
	private BindingsResults query; //result of the test query (before any updates)
	private BindingsResults queryAfterNormalUpdate;
//	private BindingsResults queryAfterFirstRollBack;
	private BindingsResults queryAfterInsertDell;
	
	
	public Inspector() {
		super();
	}
	
	public boolean isUpdateSameOfInsertDelete() {
		return areEq(queryAfterNormalUpdate,queryAfterInsertDell);
		
	}
	
	public TestResult getResult(ArrayList<TestMetric> phases, boolean excAskTest, int triples,int preInsered) {
		TestResult ris = new TestResult(phases);
		
		//-------------------------------------First check
		ris.setUpdateSameOfInsertDelete(this.isUpdateSameOfInsertDelete());


		//-------------------------------------samples
		ris.setAfterInsDelQueryTriple_example(getFirstOf(queryAfterInsertDell));
		ris.setAfterInsDelQueryTriples_count(queryAfterInsertDell!=null ? queryAfterInsertDell.size(): 0);
		ris.setAfterUpdateQueryTriple_example(getFirstOf(queryAfterNormalUpdate));
		ris.setAfterUpdateQueryTriples_count(queryAfterNormalUpdate!=null ? queryAfterNormalUpdate.size(): 0);
		ris.setAskAddedTriple_example(getFirstOf(askForInsert));
		ris.setAskAddedTriples_count(askForInsert!=null ? askForInsert.size(): 0);
		ris.setAskRemovedTriple_example(getFirstOf(askForDelete));
		ris.setAskRemovedTriples_count(askForDelete!=null ? askForDelete.size(): 0);
		ris.setPreQueryTriple_example(getFirstOf(query));
		ris.setPreQueryTriples_count(query!=null ? query.size(): 0);
		ris.setTestTripleCount(triples);
		ris.setPreInseredTripleCount(preInsered);
		
		//-------------------------------------Second check
		if(excAskTest){
			ris.setAskDeleteOk(this.isDeleteTriplesOk());
			ris.setAskInsertOk(this.isInsertTriplesOk());
		}
		
		return ris;
	}
	
	private String getFirstOf(BindingsResults br) {
		String first = "";
		if(br!=null && br.getBindings().size()>0) {
			first=br.getBindings().get(0).toJson().toString();
		}else{
			first="No triples";
		}
		return first;
	}
	
	public boolean isDeleteTriplesOk() {
		BindingsResults removed=getOuterJoinA(query,queryAfterNormalUpdate);
		return areEq(askForDelete,removed);
	}
	public boolean isInsertTriplesOk() {
		BindingsResults added=getOuterJoinA(queryAfterNormalUpdate,query);	
		return areEq(askForInsert,added);
	}


	
	public BindingsResults getAskForDelete() {
		return askForDelete;
	}

	public void setAskForDelete(BindingsResults askForDelete) {
		this.askForDelete = askForDelete;
	}

	public BindingsResults getAskForInsert() {
		return askForInsert;
	}

	public void setAskForInsert(BindingsResults askForInsert) {
		this.askForInsert = askForInsert;
	}

	public BindingsResults getQuery() {
		return query;
	}

	public void setQuery(BindingsResults query) {
		this.query = query;
	}

	public BindingsResults getQueryAfterNormalUpdate() {
		return queryAfterNormalUpdate;
	}

	public void setQueryAfterNormalUpdate(BindingsResults queryAfterNormalUpdate) {
		this.queryAfterNormalUpdate = queryAfterNormalUpdate;
	}

//	public BindingsResults getQueryAfterFirstRollBack() {
//		return queryAfterFirstRollBack;
//	}
//
//	public void setQueryAfterFirstRollBack(BindingsResults queryAfterFirstRollBack) {
//		this.queryAfterFirstRollBack = queryAfterFirstRollBack;
//	}

	public BindingsResults getQueryAfterInsertDell() {
		return queryAfterInsertDell;
	}

	public void setQueryAfterInsertDell(BindingsResults queryAfterInsertDell) {
		this.queryAfterInsertDell = queryAfterInsertDell;
	}
	
}
