using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestViewer.model
{
   public class TestResult
    {

   
		private List<Metric> phases;
		private Boolean updateSameOfInsertDelete;

		private Boolean askCheckDone;
		private Boolean askDeleteOk;
		private Boolean askInsertOk;

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

		private int testTripleCount;
		private int preInseredTripleCount;
		private int index = 0;

		public TestResult(JObject obj,int index)
        {
			this.index = index;
			phases = new List<Metric>();
			updateSameOfInsertDelete = obj.GetValue("insDelOk").Value<Boolean>();
			askCheckDone = obj.GetValue("askCheckDone").Value<Boolean>();
			askDeleteOk = obj.GetValue("askDeleteOk").Value<Boolean>();
			askInsertOk = obj.GetValue("askInsertOk").Value<Boolean>();
			askAddedTriple_example = obj.GetValue("askAddedTriple_example").Value<String>();
			askRemovedTriple_example = obj.GetValue("askRemovedTriple_example").Value<String>();
			preQueryTriple_example = obj.GetValue("preQueryTriple_example").Value<String>();
			afterUpdateQueryTriple_example = obj.GetValue("afterUpdateQueryTriple_example").Value<String>();
			afterInsDelQueryTriple_example = obj.GetValue("afterInsDelQueryTriple_example").Value<String>();
			askAddedTriples_count = obj.GetValue("askAddedTriples_count").Value<int>();
			askRemovedTriples_count = obj.GetValue("askRemovedTriples_count").Value<int>();
			preQueryTriples_count = obj.GetValue("preQueryTriples_count").Value<int>();
			afterUpdateQueryTriples_count = obj.GetValue("afterUpdateQueryTriples_count").Value<int>();
			afterInsDelQueryTriples_count = obj.GetValue("afterInsDelQueryTriples_count").Value<int>();
			testTripleCount = obj.GetValue("testTripleCount").Value<int>();
			preInseredTripleCount = obj.GetValue("preInseredTripleCount").Value<int>();
			

			JObject temp = obj.GetValue("testMetrics").Value<JObject>();
			foreach (JProperty key in temp.Properties())
			{
				phases.Add(new Metric(temp[key.Name].Value<JObject>()));
			}
		}

		public Metric getMetricByName(String name) {
			foreach (Metric m in phases)
			{
				if (m.Name==name)
				{
					return m;
				}
			}
			return null;
		}
		public int errorCount()
		{
			int count = 0;
			foreach (Metric m in phases)
			{
				if (m.Err) {
					count++;
				}
			}
			return count;
		}
		public bool UpdateSameOfInsertDelete { get => updateSameOfInsertDelete; set => updateSameOfInsertDelete = value; }
		public bool AskCheckDone { get => askCheckDone; set => askCheckDone = value; }
		public bool AskDeleteOk { get => askDeleteOk; set => askDeleteOk = value; }
		public bool AskInsertOk { get => askInsertOk; set => askInsertOk = value; }
		public string AskAddedTriple_example { get => askAddedTriple_example; set => askAddedTriple_example = value; }
		public string AskRemovedTriple_example { get => askRemovedTriple_example; set => askRemovedTriple_example = value; }
		public string PreQueryTriple_example { get => preQueryTriple_example; set => preQueryTriple_example = value; }
		public string AfterUpdateQueryTriple_example { get => afterUpdateQueryTriple_example; set => afterUpdateQueryTriple_example = value; }
		public string AfterInsDelQueryTriple_example { get => afterInsDelQueryTriple_example; set => afterInsDelQueryTriple_example = value; }
		public int AskAddedTriples_count { get => askAddedTriples_count; set => askAddedTriples_count = value; }
		public int AskRemovedTriples_count { get => askRemovedTriples_count; set => askRemovedTriples_count = value; }
		public int PreQueryTriples_count { get => preQueryTriples_count; set => preQueryTriples_count = value; }
		public int AfterUpdateQueryTriples_count { get => afterUpdateQueryTriples_count; set => afterUpdateQueryTriples_count = value; }
		public int AfterInsDelQueryTriples_count { get => afterInsDelQueryTriples_count; set => afterInsDelQueryTriples_count = value; }
		public int TestTripleCount { get => testTripleCount; set => testTripleCount = value; }
		public int PreInseredTripleCount { get => preInseredTripleCount; set => preInseredTripleCount = value; }
		internal List<Metric> Phases { get => phases; set => phases = value; }


		public override string ToString()
		{
			return "Metric test, execution N°" + index;
		}
	}
}
