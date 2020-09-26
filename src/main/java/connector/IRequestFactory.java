package connector;

import java.util.Set;

public interface IRequestFactory {

	Set<String> getRequestNames();
	
	ISparqlRequest getRequestByName(String name);
}
