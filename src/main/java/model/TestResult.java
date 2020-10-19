package model;

import java.util.ArrayList;

import com.google.gson.JsonObject;

public class TestResult {

	private ArrayList<TestMetric> phases;
	private boolean updateSameOfInsertDelete;

	private boolean askCheckDone;
	private boolean askDeleteOk;
	private boolean askInsertOk;
	
	private String askAddedTriple_example;
	private String askRemovedTriple_example;
	private String preQueryTriple_example;
	private String afterUpdateQueryTriple_example;
	private String afterInsDelQueryTriple_example;
	
	private int askAddedTriples_count;
	private int askRemovedTriples_count;
	private int preQueryTriples_count;
	private int afterUpdateQueryTriples_count;
	private int afterInsDelQueryTriples_count;
	
	
	public TestResult(ArrayList<TestMetric> phases) {
		super();
		this.phases = phases;
		this.askCheckDone=false;
		this.askDeleteOk=false;
		this.askInsertOk=false;
	}
	
	public JsonObject toJson() {
		JsonObject ris = new JsonObject();
		ris.addProperty("askCheckDone", askCheckDone);
		ris.addProperty("askDeleteOk", askDeleteOk);
		ris.addProperty("askInsertOk", askInsertOk);
		ris.addProperty("askAddedTriples_count", askAddedTriples_count);
		ris.addProperty("askRemovedTriples_count", askRemovedTriples_count);
		ris.addProperty("preQueryTriples_count", preQueryTriples_count);
		ris.addProperty("afterUpdateQueryTriples_count", afterUpdateQueryTriples_count);
		ris.addProperty("afterInsDelQueryTriples_count", afterInsDelQueryTriples_count);
		ris.addProperty("askAddedTriple_example", askAddedTriple_example);
		ris.addProperty("askRemovedTriple_example", askRemovedTriple_example);
		ris.addProperty("preQueryTriple_example", preQueryTriple_example);
		ris.addProperty("afterUpdateQueryTriple_example", afterUpdateQueryTriple_example);
		ris.addProperty("afterInsDelQueryTriple_example", afterInsDelQueryTriple_example);

		JsonObject testMetricsJson = new JsonObject();
		int x = 0;
		for (TestMetric testMetric : phases) {
			JsonObject metricJson = new JsonObject();
			metricJson.addProperty("name", testMetric.getName());
			metricJson.addProperty("iterator", x);
			metricJson.addProperty("value",  testMetric.getInterval());
			metricJson.addProperty("error",  testMetric.isError());
			testMetricsJson.add("metric_"+x, metricJson);
			x++;
		}
		ris.add("testMetrics",testMetricsJson);
		return ris;
	}
	
	//----------------------------------------------------GETTERS AND SETTERS
	public ArrayList<TestMetric> getPhases() {
		return phases;
	}
	public void setPhases(ArrayList<TestMetric> phases) {
		this.phases = phases;
	}
	public boolean isUpdateSameOfInsertDelete() {
		return updateSameOfInsertDelete;
	}
	public void setUpdateSameOfInsertDelete(boolean updateSameOfInsertDelete) {
		this.updateSameOfInsertDelete = updateSameOfInsertDelete;
	}
	public boolean isAskCheckDone() {
		return askCheckDone;
	}
	public void setAskCheckDone(boolean askCheckDone) {
		this.askCheckDone = askCheckDone;
	}
	public boolean isAskDeleteOk() {
		return askDeleteOk;
	}
	public void setAskDeleteOk(boolean askDeleteOk) {
		this.askCheckDone=true;
		this.askDeleteOk = askDeleteOk;
	}
	public boolean isAskInsertOk() {
		return askInsertOk;
	}
	public void setAskInsertOk(boolean askInsertOk) {
		this.askCheckDone=true;
		this.askInsertOk = askInsertOk;
	}
	public String getAskAddedTriple_example() {
		return askAddedTriple_example;
	}
	public void setAskAddedTriple_example(String askAddedTriple_example) {
		this.askAddedTriple_example = askAddedTriple_example;
	}
	public String getAskRemovedTriple_example() {
		return askRemovedTriple_example;
	}
	public void setAskRemovedTriple_example(String askRemovedTriple_example) {
		this.askRemovedTriple_example = askRemovedTriple_example;
	}
	public String getPreQueryTriple_example() {
		return preQueryTriple_example;
	}
	public void setPreQueryTriple_example(String preQueryTriple_example) {
		this.preQueryTriple_example = preQueryTriple_example;
	}
	public String getAfterUpdateQueryTriple_example() {
		return afterUpdateQueryTriple_example;
	}
	public void setAfterUpdateQueryTriple_example(String afterUpdateQueryTriple_example) {
		this.afterUpdateQueryTriple_example = afterUpdateQueryTriple_example;
	}
	public String getAfterInsDelQueryTriple_example() {
		return afterInsDelQueryTriple_example;
	}
	public void setAfterInsDelQueryTriple_example(String afterInsDelQueryTriple_example) {
		this.afterInsDelQueryTriple_example = afterInsDelQueryTriple_example;
	}
	public int getAskAddedTriples_count() {
		return askAddedTriples_count;
	}
	public void setAskAddedTriples_count(int askAddedTriples_count) {
		this.askAddedTriples_count = askAddedTriples_count;
	}
	public int getAskRemovedTriples_count() {
		return askRemovedTriples_count;
	}
	public void setAskRemovedTriples_count(int askRemovedTriples_count) {
		this.askRemovedTriples_count = askRemovedTriples_count;
	}
	public int getPreQueryTriples_count() {
		return preQueryTriples_count;
	}
	public void setPreQueryTriples_count(int preQueryTriples_count) {
		this.preQueryTriples_count = preQueryTriples_count;
	}
	public int getAfterUpdateQueryTriples_count() {
		return afterUpdateQueryTriples_count;
	}
	public void setAfterUpdateQueryTriples_count(int afterUpdateQueryTriples_count) {
		this.afterUpdateQueryTriples_count = afterUpdateQueryTriples_count;
	}
	public int getAfterInsDelQueryTriples_count() {
		return afterInsDelQueryTriples_count;
	}
	public void setAfterInsDelQueryTriples_count(int afterInsDelQueryTriples_count) {
		this.afterInsDelQueryTriples_count = afterInsDelQueryTriples_count;
	}
	

	
}
