package support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.jena.base.Sys;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NsIterator;

import connector.SparqlRequest;
import it.unibo.arces.wot.sepa.commons.response.Response;

public class LumbToSparql {

	private String bigString = "";
	private long size = 0;
	private String graph;
	private boolean isError;
	
	public LumbToSparql() {
		super();
		this.graph= "<http://lumb/for.sepa.test/workspace/defaultgraph>";
	}
	
	public LumbToSparql(String graph) {
		super();
		this.graph= graph;
	}

	public String getBigString() {
		return bigString;
	}

	public void setBigString(String bigString) {
		this.bigString = bigString;
	}
	
	

	public void println(String str){
		bigString+="\n"+str;
	}
	public void println(){
		bigString+="\n";
	}
	public void print(String str){
		bigString+=str;
	}
		
	
	
	public boolean isError() {
		return isError;
	}

	public long getSize() {
		return size;
	}

	public void close() {
		//-------------------------Convert to TURTLE-----------------------------
		Model model = ModelFactory.createDefaultModel() ;
		
		InputStream in = new ByteArrayInputStream(bigString.getBytes(StandardCharsets.UTF_8));
		model.read(in,"RDF/XML");
		//System.out.println("triple1: "+model.listStatements().next().asTriple().toString());
		
		ByteArrayOutputStream temp = new ByteArrayOutputStream();
		model.write(temp, "TURTLE");	
		size = model.size();
		System.out.println("LumbToSparql, Turtle size: "+ size);
		//--------------------------convert to sparql and execute Insert Data
		bigString=new String(temp.toByteArray(),StandardCharsets.UTF_8);
		// insert on sparql and dispose ///WIP
		TTLInsertData insertData = new TTLInsertData(graph);
		int prefixCharCount = 0;
		StringTokenizer st = new StringTokenizer(bigString,"\n");
		boolean notEndPrefixs=true;
		while(st.hasMoreTokens() && notEndPrefixs) {
			String pref = st.nextToken();
			if(pref.startsWith("@prefix")) {
				insertData.convertAndAddPrefix(pref);
				prefixCharCount+=pref.length()+1;
			}else if(pref.trim().length()>0) {
				notEndPrefixs=false;
			}else{
				prefixCharCount+=pref.length()+1;
			}
		}
		insertData.setTriples(bigString.substring(prefixCharCount));
		bigString="";
		
		//sparql inser data
		isError=!insertData.doInsertData();
		
	}
	

	
}
