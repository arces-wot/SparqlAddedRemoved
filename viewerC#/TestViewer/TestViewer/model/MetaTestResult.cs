using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestViewer.model
{
   public class MetaTestResult
    {
        private int tripleNumber;
        private int replication;
        private List<TestResult> tests;
        private List<Metric> avarages;
        private String name;

        public MetaTestResult(JObject obj) {
            tests = new List<TestResult>();
            tripleNumber = obj.GetValue("TRIPLE_NUMBER").Value<int>();
            replication = obj.GetValue("REPLICATION").Value<int>();
            name = obj.GetValue("NAME_MT").Value<String>();
            JObject temp = obj.GetValue("TESTS").Value<JObject>();
            int index = 1;
            foreach (JProperty item in temp.Children())
            {
                tests.Add(new TestResult(temp[item.Name].Value<JObject>(),index));
                index++;
            }
            temp = obj.GetValue("AVARAGES").Value<JObject>();
            avarages = new List<Metric>();
            foreach (JProperty item in temp.Children())
            {
                avarages.Add(new Metric(temp[item.Name].Value<JObject>()));
            }
        }

        public int errorCount() {
            int count = 0;
            foreach (TestResult tr in Tests) {
                count += tr.errorCount();
            }
            return count;
        }
        public int warningCount()
        {
            int count = 0;
            foreach (TestResult tr in Tests)
            {
                count += tr.warningCount();
            }
            return count;
        }
        public int TripleNumber { get => tripleNumber; set => tripleNumber = value; }
        public int Replication { get => replication; set => replication = value; }
        public string Name { get => name; set => name = value; }
        internal List<TestResult> Tests { get => tests; set => tests = value; }
        internal List<Metric> Avarages { get => avarages; set => avarages = value; }


        public override string ToString()
        {
            return "MetaTest with " + TripleNumber + " triple and " + Replication +" executions";
        }
    }
}
