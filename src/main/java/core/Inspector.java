package core;

import com.google.gson.JsonObject;

import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;

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
	
	public static BindingsResults getBRemovedFromA(BindingsResults B,BindingsResults A) {
		BindingsResults ris= new BindingsResults(new JsonObject());
		for(Bindings bindings : B.getBindings()){
			if(!A.contains(bindings)) {
				//se A non contiene il bindings in B significa che è stato rimosso da B (tramite l'update)
				ris.add(bindings);
			}
		}
		return ris;
	}
	public static BindingsResults getBAddedFromA(BindingsResults B,BindingsResults A) {
		BindingsResults ris=new BindingsResults(new JsonObject());
		for(Bindings bindings : A.getBindings()){
			System.out.println("------OK");
			if(!B.contains(bindings)) {

				System.out.println("------MIAO");
				//se B non contiene il bindings in A significa che è stato aggiunto a B (tramite l'update)
				ris.add(bindings);
			}
		}
		return ris;
	}
	
	private BindingsResults askForDelete; 	//removed
	private BindingsResults askForInsert;	//added
	private BindingsResults query; //result of the test query (before any updates)
	private BindingsResults queryAfterNormalUpdate;
//	private BindingsResults queryAfterFirstRoolBack;
	private BindingsResults queryAfterInsertDell;
	
	
	public Inspector() {
		super();
	}
	
	public boolean isUpdateSameOfInsertDelete() {
		internalPrint();
		return areEq(queryAfterNormalUpdate,queryAfterInsertDell);
		
	}
	
	public boolean isDeleteTriplesOk() {
		BindingsResults removed=getBRemovedFromA(query,queryAfterNormalUpdate);
		return areEq(askForDelete,removed);
	}
	public boolean isInsertTriplesOk() {
		BindingsResults added=getBAddedFromA(query,queryAfterNormalUpdate);
		return areEq(askForInsert,added);
	}

	private void internalPrint() {
		System.out.println("----------------------------------printComparationInsertTriplesOk-----INIZIO");
		
		System.out.println("--------------------------------getBAddedFromA(query,queryAfterNormalUpdate)------------------------------------");
		for(Bindings bindings : getBAddedFromA(query,queryAfterNormalUpdate).getBindings()){
			System.out.println(bindings.toJson().toString());
		}
		System.out.println("----------------------getBRemovedFromA(query,queryAfterNormalUpdate)-----------------------------");
		for(Bindings bindings : getBRemovedFromA(query,queryAfterNormalUpdate).getBindings()){
			System.out.println(bindings.toJson().toString());
		}
		System.out.println("----------------------------------printComparationInsertTriplesOk-----FINE");
		
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

//	public BindingsResults getQueryAfterFirstRoolBack() {
//		return queryAfterFirstRoolBack;
//	}
//
//	public void setQueryAfterFirstRoolBack(BindingsResults queryAfterFirstRoolBack) {
//		this.queryAfterFirstRoolBack = queryAfterFirstRoolBack;
//	}

	public BindingsResults getQueryAfterInsertDell() {
		return queryAfterInsertDell;
	}

	public void setQueryAfterInsertDell(BindingsResults queryAfterInsertDell) {
		this.queryAfterInsertDell = queryAfterInsertDell;
	}
	
}
