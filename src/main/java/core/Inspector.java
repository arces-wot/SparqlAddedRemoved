package core;

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
			return true;
		}else {
			return false;
		}
	}
}
