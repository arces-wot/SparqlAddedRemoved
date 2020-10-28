using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestViewer.model.jsap
{
    class JsapUpdate : IJsapRequest
    {
        private String sparql;
        private List<JsapBind> bindings;
        public JsapUpdate(JObject obj) {

            sparql = obj.GetValue("sparql").Value<String>();
            bindings = new List<JsapBind>();
            if (obj.ContainsKey("forcedBindings")) {
                JObject listOfBinds = obj.GetValue("forcedBindings").Value<JObject>();
                foreach (JProperty bind in listOfBinds.Children())
                {
                    bindings.Add(new JsapBind(bind.Name, listOfBinds.GetValue(bind.Name).Value<JObject>()));
                }
            }
         
            //obj.GetValue("forcedBindings");
        }
        public List<JsapBind> getBinds()
        {
            return bindings;
        }

        public string getSparql()
        {
            String ris = sparql;
            foreach (JsapBind bind in bindings)
            {
                //attenzione non tengo gonto del tipo (bind.type)
                ris = ris.Replace("?" + bind.Name, bind.Value +".\n");
            }
            return ris;
        }

        public string getNotResolvedSparql()
        {
            return sparql;
        }
    }
}
