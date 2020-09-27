package support;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.bouncycastle.util.Arrays;

import connector.SparqlRequest;
import it.unibo.arces.wot.sepa.commons.response.Response;
import model.EndPoint;
import model.SparqlObj;

public class TTLInsertData {
	private static int maxLines = 1000;
	
	private ArrayList<String> prefixs = new ArrayList<String>();
	private String graph;
	private String triples;
	
	
	public TTLInsertData(String graph) {
		super();
		this.graph = graph;
	}
	
	
	public void convertAndAddPrefix(String pref) {
		String temp=pref.replace("@prefix","PREFIX");
		int myTrimX=0;
		for(int x=temp.length()-1;x>0;x--) {
			if(!(temp.charAt(x)=='.' || temp.charAt(x)==' ')){			
				break;
			}	
			myTrimX=x;
		}
		String flitered = temp.substring(0,myTrimX);
		//System.out.println("FILTERED: "+flitered);
		prefixs.add(flitered);
	}


	public String getTriples() {
		return triples;
	}


	public void setTriples(String triples) {
		this.triples = triples;
	}
	
	public void doInsertData(){
		String[] lines=this.triples.split("\n");
		if(lines.length<=maxLines){
			System.out.println("-->"+lines.length );
			insertDataSplit(this.triples);
		}else{
			int splitSplitLinesCount = 0;
			int actualPartLineCount=0;
			String actualSplit ="";
			String actualPart ="";
			int nPartSize = (int) Math.ceil((double)lines.length/(double)maxLines);
			int seed =maxLines;
			int oldSeed=0;
			for(int x=0;x<nPartSize;x++) {
				boolean notFound=true;
				while(notFound) {
					String actualL = lines[seed];
					if(actualL.trim().length()==0){
						notFound=false;					
						insertDataSplit(stringify(lines, oldSeed, seed));
					}else{
						seed--;
					}
				}				
				oldSeed=seed+1;
				seed+=maxLines;
				if(seed>lines.length) {
					insertDataSplit(stringify(lines, oldSeed, lines.length));
					break;
				}
			}
		}
	}
	
	private String stringify(String[] lines, int start,int stop) {
		System.out.println("--->"+start+ "   :    "+stop);
		String ris = "";
		for(int x=start;x<stop;x++) {
			ris+=lines[x];
		}
		return ris;
	}
	
	private void insertDataSplit(String t) {
		String head = "";
		for (String string : prefixs) {
			head+=string+"\n";
		}
		head+="INSERT DATA  {   GRAPH " + graph+" {\n";		
		
		String sparql=head+"\n"+t + "\n}}";		
		EndPoint endPointHost= new EndPoint("http","localhost",8000,"/update");
		Response res = new SparqlRequest(new SparqlObj(sparql),endPointHost).execute();
		System.out.println("InsertData success: "+!res.isError());
	}
	
	
}
