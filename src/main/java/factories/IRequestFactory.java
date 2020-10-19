package factories;

import java.util.Set;

import connector.ISparqlRequest;

public interface IRequestFactory {

	Set<String> getRequestNames();
	
	ISparqlRequest getRequestByName(String name);
}
