using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestViewer.model.jsap
{
    public class Jsap
    {

        Dictionary<String,IJsapRequest> requests;
        Dictionary<String, JsapTest> tests;
        List<String> sparqlTestPhasesName;
        public Jsap(JObject obj)
        {
            requests = new Dictionary<String, IJsapRequest>();
            tests = new Dictionary<String, JsapTest>();
            sparqlTestPhasesName = new List<string>();
            sparqlTestPhasesName.Add("Update");
            sparqlTestPhasesName.Add("Query");
            sparqlTestPhasesName.Add("Rollback");
            sparqlTestPhasesName.Add("Pre-Update");
            sparqlTestPhasesName.Add("Pre-Update rollback");
            JObject temp =obj.GetValue("extended").Value<JObject>().GetValue("tests").Value<JObject>();
            foreach (JProperty test in temp.Children())
            {
                tests.Add(test.Name,new JsapTest(temp.GetValue(test.Name).Value<JObject>()));
            }
            JObject updates = obj.GetValue("updates").Value<JObject>();
            foreach (JProperty update in updates.Children())
            {
                requests.Add(update.Name, new JsapUpdate(updates.GetValue(update.Name).Value<JObject>()));
            }
            JObject queries = obj.GetValue("queries").Value<JObject>();
            foreach (JProperty query in queries.Children())
            {
                requests.Add(query.Name, new JsapQuery(queries.GetValue(query.Name).Value<JObject>()));
            }
          
        }

        public String getSparqlOf(String testName, String sparqlTestPhaseName) {
            JsapTest test;
            if (tests.TryGetValue(testName,out test))
            {
                IJsapRequest req;
                if (requests.TryGetValue(test.getLinkByName(sparqlTestPhaseName),out req)) {
                    return req.getSparql();
                }
                else {

                    return "Test not found for: "+ sparqlTestPhaseName;
                }

            }
            else {
                return "Test not found for: "+ testName;
            }
        }
        public List<String> getSparqlPhasesName() {
            return sparqlTestPhasesName;
        }

   }


 
  
}
