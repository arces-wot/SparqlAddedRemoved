package core.request;

import connector.SparqlRequest;

public interface IMetaSparqlRequest {

	SparqlRequest generate(int triples)throws CloneNotSupportedException;
}
