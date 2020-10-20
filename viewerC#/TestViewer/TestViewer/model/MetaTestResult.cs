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
        private int index;
        private List<Metric> avarages;

        public MetaTestResult(JObject obj,int index) {
            this.index = index;
            tests = new List<TestResult>();
            tripleNumber = obj.GetValue("TRIPLE_NUMBER").Value<int>();
            replication = obj.GetValue("REPLICATION").Value<int>();
            JObject temp = obj.GetValue("TESTS").Value<JObject>();
            foreach (JProperty item in temp.Children())
            {
                tests.Add(new TestResult(temp[item.Name].Value<JObject>()));
            }
            temp = obj.GetValue("AVARAGES").Value<JObject>();
            avarages = new List<Metric>();
            foreach (JProperty item in temp.Children())
            {
                avarages.Add(new Metric(temp[item.Name].Value<JObject>()));
            }
        }

        public int TripleNumber { get => tripleNumber; set => tripleNumber = value; }
        public int Replication { get => replication; set => replication = value; }
        internal List<TestResult> Tests { get => tests; set => tests = value; }
        internal List<Metric> Avarages { get => avarages; set => avarages = value; }

        public override string ToString()
        {
            return "MetaTest_" + this.index;
        }
    }
}
