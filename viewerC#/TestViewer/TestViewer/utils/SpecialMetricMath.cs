using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TestViewer.model;

namespace TestViewer.utils
{

    public enum Op
    {
        ADD,
        SUB,
        MUL,
        DIV
    }

    public class SpecialMetricMath : SpecialMetric
    {
        private Dictionary<string, Op> realMetricOP;
        public SpecialMetricMath(string name, Dictionary<string, Op> realMetric) : base(name, realMetric.Keys.ToList<string>())
        {
            this.realMetricOP = realMetric;
        }
        public override double getValue(TestResult tr)
        {
            double ris = 0;
            foreach (string metricName in base.RealMetric) {
                Metric m = tr.getMetricByName(metricName);
                if (m != null)
                {
                    Op op;
                    if (realMetricOP.TryGetValue(metricName, out op)) {
                        if (op == Op.ADD) {
                            ris += m.Value;
                        } else if (op == Op.SUB) {
                            ris -= m.Value;
                        } else if (op == Op.DIV) {
                            ris /= m.Value;
                        } else if (op == Op.MUL)
                        {
                            ris *= m.Value;
                        }
                    }
                }
                else {
                    Console.WriteLine("Warning on SpecialMetricSum, metric not found!");
                }

            }
            return ris;
        }

        public override string ToString()
        {
            return "Special Metric Math: " + base.Name;
        }

    }
}
