package model;

import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;

public class AddedRemoved {

	private BindingsResults added;
	private BindingsResults removed;
	
	
	
	
	public AddedRemoved(BindingsResults added, BindingsResults removed) {
		super();
		this.added = added;
		this.removed = removed;
	}
	
	
	public BindingsResults getAdded() {
		return added;
	}
	public void setAdded(BindingsResults added) {
		this.added = added;
	}
	public BindingsResults getRemoved() {
		return removed;
	}
	public void setRemoved(BindingsResults removed) {
		this.removed = removed;
	}
	
}
