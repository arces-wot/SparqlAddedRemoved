package core;

import java.io.File;
import java.io.IOException;

import factories.JsapMetaTestFactory;

public class JSAPTestRunner {
	
	public static void main (String[] args) {
		ITestVisitor monitor=null;
		try {				
			monitor = new TestVisitorOutputJsonFile(new File("./output.json").getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (String testName : JsapMetaTestFactory.getInstance().getTestNames()) {
			MetaTest MT1 = JsapMetaTestFactory.getInstance().getTestByName(testName);
			MT1.setMonitor(monitor);
			System.out.println("Test "+testName+ " Start.");
			MT1.execute();
			System.out.println("Test "+testName+ " end.");			
		}

		//close 
		monitor.close();
	}
	

	
}
