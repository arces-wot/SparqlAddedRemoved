package core;

import connector.SparqlRequest;

public interface IMetaSparqlRequest {

	SparqlRequest generate(int triples);
}
