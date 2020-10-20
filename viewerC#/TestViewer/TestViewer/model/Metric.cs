using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestViewer.model
{
   public class Metric
    {
        private String name;
        private int interation;
        private Boolean err;
        private Boolean erroUndef = true;
        private int value;

        public Metric(JObject obj) {
            if (obj.GetValue("error")!=null) {
                err = obj.GetValue("error").Value<Boolean>();
                erroUndef = false;
            }
            interation = obj.GetValue("iterator").Value<int>();
            value = obj.GetValue("value").Value<int>();
            name = obj.GetValue("name").Value<String>();
        }

        public string Name { get => name; set => name = value; }
        public int Interation { get => interation; set => interation = value; }
        public bool Err { get => err; set => err = value; }
        public int Value { get => value; set => this.value = value; }

        public object[] toObjArray() {
            object[] ris = new object[4];
            ris[0] = Name;
            ris[1] = value;
            ris[2] = interation;
            if (erroUndef)
            {
                ris[3] = "";
            }
            else
            {
                ris[3] = err;
            }
            return ris;
        }

        public override string ToString()
        {
            return "Metric of " + this.name;
        }
    }
}
