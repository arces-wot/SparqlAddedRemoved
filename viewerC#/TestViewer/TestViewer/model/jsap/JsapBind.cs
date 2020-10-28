using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestViewer.model.jsap
{
    class JsapBind
    {

        private String name;
        private String type;
        private String value;

        public JsapBind(String name, JObject obj) {
            this.name = name;

            type = obj.GetValue("type").Value<String>();
            value = obj.GetValue("value").Value<String>();

        }
        public string Name { get => name; set => name = value; }
        public string Type { get => type; set => type = value; }
        public string Value { get => value; set => this.value = value; }
    }
}
