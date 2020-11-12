package addedremoved.epspec;



public class EpSpecFactory {

	public enum EndPointSpec {
		VIRTUOSO("VIRTUOSO"),
		BLAZEGRAPH("BLAZEGRAPH");		
		private final String eps;
		 
		EndPointSpec(final String eps) {
		        this.eps = eps;
	    }
	  
	    @Override
	    public String toString() {
	        return eps;
	    }
		    
	}
	

	
	
	private static IEndPointSpecification instance = new VirtuosoSpecification();
	
	public static IEndPointSpecification getInstance() {
			return instance;
	}
	
	public static void setInstance(EndPointSpec eps){
			if(eps==EndPointSpec.BLAZEGRAPH) {
				instance = new BlazegraphSpecification();
			}else{// default--> (eps==EndPointSpec.VIRTUOSO) {
				instance = new VirtuosoSpecification();
			}
	}
	
	public static void setInstance(String eps){
		String fixed = eps.trim().toUpperCase().replace("\"", "");
		if(fixed.compareTo(EndPointSpec.BLAZEGRAPH.toString())==0) {
			instance = new BlazegraphSpecification();
		}else{// default--> (eps==EndPointSpec.VIRTUOSO) {
			instance = new VirtuosoSpecification();
		}
}
}
