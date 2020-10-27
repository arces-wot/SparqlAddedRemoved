using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestViewer.model.jsap
{
    class JsapTest
    {

        private String PreUpdateLink;
        private String PreUpdateRollbackLink;
        private String UpdateLink;
        private String RollbackLink;
        private String QueryLink;

        public JsapTest(JObject obj)
        {
            PreUpdateLink = obj.GetValue("PreUpdateLink").Value<String>();
            PreUpdateRollbackLink = obj.GetValue("PreUpdateRollbackLink").Value<String>();
            UpdateLink = obj.GetValue("UpdateLink").Value<String>();
            RollbackLink = obj.GetValue("RollbackLink").Value<String>();
            QueryLink = obj.GetValue("QueryLink").Value<String>();
        }

        public string PreUpdateLink1 { get => PreUpdateLink; set => PreUpdateLink = value; }
        public string PreUpdateRollbackLink1 { get => PreUpdateRollbackLink; set => PreUpdateRollbackLink = value; }
        public string UpdateLink1 { get => UpdateLink; set => UpdateLink = value; }
        public string RollbackLink1 { get => RollbackLink; set => RollbackLink = value; }
        public string QueryLink1 { get => QueryLink; set => QueryLink = value; }

        public String getLinkByName(String name) {
            switch (name)
            {
                case "Update":
                    return UpdateLink;
                case "Query":
                    return QueryLink;
                case "Rollback":
                    return RollbackLink;
                case "Pre-Update":
                    return PreUpdateLink;
                case "Pre-Update rollback":
                    return PreUpdateRollbackLink;
                default:
                    return "";
            }
        }

        
    }
}
