package core;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import model.TestMetric;
import model.TestResult;

public class TestVisitorOutputJsonFile implements ITestVisitor {
	//----------JSON fields
	private static String TRIPLE_NUMBER="TRIPLE_NUMBER";
	private static String REPLICATION="REPLICATION";
	private static String TESTS="TESTS";
	private static String AVARAGES="AVARAGES";
	//--------------------
	
	
	private String filePath;
	private boolean isClosed=false;
	private FileWriter fileIO; 
	private int testID =0;
	private int subTestID=0;
	private boolean firstTestVisit = true;
	private boolean firstSubTestVisit = true;
	
	public TestVisitorOutputJsonFile(String filePath) throws IOException {
		super();
		this.filePath = filePath;
		this.isClosed=false;
		fileIO = new FileWriter(this.filePath);
		writeLineOnFile("{");//OPEN 0
	}
	
	public void start(int n, int replication) {
		String str = "'"+testID + "':{";
		testID++;
		subTestID=0;
		if(firstTestVisit) {
			firstTestVisit=false;
		}else {
			str=","+str;
		}
		writeLineOnFile(str); //OPEN 1
		writeLineOnFile("'"+TRIPLE_NUMBER + "':"+n+",");
		writeLineOnFile("'"+REPLICATION + "':"+replication+",");
		writeLineOnFile("'"+TESTS + "':{");//OPEN 2
	}

	public void visit(TestResult res) {
		String str = "'"+subTestID + "':"+res.toJson().toString();
		subTestID++;
		if(firstSubTestVisit) {
			firstSubTestVisit = false;
		}else {
			str=","+str;
		}
		writeLineOnFile(str);
	}

	public void visit(ArrayList<TestMetric> res) {

		writeLineOnFile("},");//OPEN 2
		writeLineOnFile("'"+AVARAGES + "': {");
		for(int x =0;x<res.size();x++) {
			JsonObject metricJson = new JsonObject();
			metricJson.addProperty("name", res.get(x).getName());
			metricJson.addProperty("iterator", x);
			metricJson.addProperty("value",  res.get(x).getInterval());
			if(x==0) {
				writeLineOnFile("'"+x+"':"+metricJson.toString());
			}else {
				writeLineOnFile(",'"+x+"':"+metricJson.toString());
			}
		}
		writeLineOnFile("}");
	}

	
	public void end() {
		writeLineOnFile("}");//OPEN 1
		firstSubTestVisit = true;
	}



	public void close()  {
		try {
			writeLineOnFile("}");//OPEN 0
			fileIO.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isClosed=true;
	}

	private void writeLineOnFile(String str) {
		try {
			fileIO.write(str+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//---------------------------------------setters and getters

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isClosed() {
		return isClosed;
	}

}
