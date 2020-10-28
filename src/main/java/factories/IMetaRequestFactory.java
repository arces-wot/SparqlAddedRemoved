package factories;

import java.util.Set;

import core.request.IMetaSparqlRequest;

public interface IMetaRequestFactory {
	Set<String> getRequestNames();
	
	IMetaSparqlRequest getRequestByName(String name);
}
