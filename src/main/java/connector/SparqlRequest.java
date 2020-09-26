package connector;
import it.unibo.arces.wot.sepa.commons.protocol.SPARQL11Properties.HTTPMethod;

import java.util.Set;

import it.unibo.arces.wot.sepa.commons.protocol.SPARQL11Protocol;
import it.unibo.arces.wot.sepa.commons.request.QueryRequest;
import it.unibo.arces.wot.sepa.commons.request.UpdateRequest;
import it.unibo.arces.wot.sepa.commons.response.Response;
import model.EndPoint;
import model.SparqlObj;


public class SparqlRequest implements ISparqlRequest{

	private SPARQL11Protocol client;
	private SparqlObj sparql;
	private EndPoint endPointHost;
	private int timeOut =60000;
	
	public SparqlRequest( SparqlObj sparql, EndPoint endPointHost) {
		super();
		this.sparql = sparql;
		this.endPointHost =endPointHost;
		client = new SPARQL11Protocol();//client security manager not implemented yes (as argument)
	}

	public SparqlObj getSparql() {
		return sparql;
	}

	public void setSparql(SparqlObj sparql) {
		this.sparql = sparql;
	}

	
	public EndPoint getEndPointHost() {
		return endPointHost;
	}

	public void setEndPointHost(EndPoint endPointHost) {
		this.endPointHost = endPointHost;
	}

	
	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}


	public Response execute() {	
		if(endPointHost.getPath()=="/update") {
			
			UpdateRequest req = new UpdateRequest(
					HTTPMethod.POST,
					endPointHost.getScheme(),
					endPointHost.getHost(),
					endPointHost.getPort(),
					endPointHost.getPath(),
					sparql.getSparql(),
					sparql.getDefault_graph_uri(),
					sparql.getNamed_graph_uri(),
					endPointHost.getAuthorization(),
					timeOut,
					1);

			return client.update(req);
			
		}else{
			
			QueryRequest req = new QueryRequest(
					HTTPMethod.POST,
					endPointHost.getScheme(),
					endPointHost.getHost(),
					endPointHost.getPort(),
					endPointHost.getPath(),
					sparql.getSparql(),
					sparql.getDefault_graph_uri(),
					sparql.getNamed_graph_uri(),
					endPointHost.getAuthorization(),
					timeOut,
					1);

			return client.query(req);
			
		}

	}

}
