using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TestViewer.model;

namespace TestViewer.utils
{
    public class SpecialMetricSum : SpecialMetric
    {
        public SpecialMetricSum(string name, List<string> realMetric) : base(name, realMetric)
        {
        }
        public override double getValue(TestResult tr)
        {
            double ris = 0;
            foreach (string metricName in base.RealMetric) {
                Metric m = tr.getMetricByName(metricName);
                if (m != null)
                {
                    ris += m.Value;
                }
                else {
                    Console.WriteLine("Warning on SpecialMetricSum, metric not found!");
                }

            }
            return ris;
        }

        public override string ToString()
        {
            return "Special Metric SUM: " + base.Name;
        }

    }
}
