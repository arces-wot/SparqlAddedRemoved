using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TestViewer.model;

namespace TestViewer.utils
{
    public class SpecialMetric
    {

        private String name;
        private List<String> realMetric;

        public SpecialMetric(string name, List<string> realMetric)
        {
            this.name = name;
            this.realMetric = realMetric;
        }

        public string Name { get => name; set => name = value; }
        public List<string> RealMetric { get => realMetric; set => realMetric = value; }

        public virtual double getValue(TestResult tr) {
            return 0;
        }
        public override string ToString()
        {
            return "Special Metric: " + this.Name;
        }
    }
}
