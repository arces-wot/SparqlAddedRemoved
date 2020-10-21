using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestViewer.model
{
   public class MetaTestGroup
    {
        List<MetaTestResult> list;
        String metaTestName;

        public MetaTestGroup(string metaTestName)
        {
            list = new List<MetaTestResult>();
            this.metaTestName = metaTestName;
        }

        public List<MetaTestResult> List { get => list; set => list = value; }
        public string MetaTestName { get => metaTestName; set => metaTestName = value; }

        public override string ToString()
        {
            return "MetaTest: " + this.metaTestName;
        }
        public int errorCount() {
            int count = 0;
            foreach (MetaTestResult mtr in list) {
                count += mtr.errorCount();
            }
            return count;
        }
        public int warningCount()
        {
            int count = 0;
            foreach (MetaTestResult mtr in list)
            {
                count += mtr.warningCount();
            }
            return count;
        }
    }
}
