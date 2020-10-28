using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestViewer.model.jsap
{
    class JsapQuery : IJsapRequest
    {
        private String sparql;
        private List<JsapBind> bindings;
        public JsapQuery(JObject obj)
        {
            sparql = obj.GetValue("sparql").Value<String>();
            bindings = new List<JsapBind>();
            if (obj.ContainsKey("forcedBindings"))
            {
                JObject listOfBinds = obj.GetValue("forcedBindings").Value<JObject>();
                foreach (JProperty bind in listOfBinds.Children())
                {
                    bindings.Add(new JsapBind(bind.Name, listOfBinds.GetValue(bind.Name).Value<JObject>()));
                }
            }
           
        }
        public List<JsapBind> getBinds()
        {
            return bindings;
        }

        public string getSparql()
        {
            String ris = sparql;
            foreach (JsapBind bind in bindings) {
                //attenzione non tengo gonto del tipo (bind.type)
                ris = ris.Replace("?"+bind.Name, bind.Value);
            }
            return ris;
        }
        public string getNotResolvedSparql()
        {
            return sparql;
        }
    }
}
