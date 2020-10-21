package factories;

import java.util.Set;

import core.IMetaSparqlRequest;

public interface IMetaRequestFactory {
	Set<String> getRequestNames();
	
	IMetaSparqlRequest getRequestByName(String name);
}
