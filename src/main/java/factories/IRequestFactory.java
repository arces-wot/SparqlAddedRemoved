package factories;

import java.util.Set;

import connector.ISparqlRequest;

public interface IRequestFactory {

	Set<RequestName> getRequestNames();
	
	ISparqlRequest getRequestByName(RequestName name);
}
