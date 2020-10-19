package core;

import java.util.ArrayList;

import model.TestMetric;
import model.TestResult;

public interface ITestVisitor {

	void start(int n,int replication);
	void visit(TestResult res);
	void visit(ArrayList<TestMetric> res);
	void end();
	void close();
}
