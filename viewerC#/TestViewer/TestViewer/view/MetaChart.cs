using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using TestViewer.model;

namespace TestViewer.view
{
    public partial class MetaChart : UserControl
    {
        private MetaTestGroup group;
        public MetaChart()
        {
            InitializeComponent();
        }
        public void loadMetaTest(MetaTestGroup group)
        {
            this.group = group;
            checkedListBox1.Items.Clear();
            chart1.Series.Clear();
            //ottengo la lista delle fasi (dando per scontato che sia la medesima per tutti i test)
            if (group.List.Count>0) {
                if (group.List.ElementAt(0).Tests.Count>0) {
                    foreach (Metric m in group.List.ElementAt(0).Tests.ElementAt(0).Phases) {
                        checkedListBox1.Items.Add(m.Name);
                    }
                }
            }
        }



        private void MetaChart_Load(object sender, EventArgs e)
        {
            chart1.Series.Clear();
            checkedListBox1.ItemCheck += checkedListBox1_Changed;
        }

        public void setMaxY(int max) {
            if (max > 0)
            {
                chart1.ChartAreas[0].AxisY.Maximum = max;
            }
            else
            {
                chart1.ChartAreas[0].AxisY.Maximum = double.NaN;
                chart1.ChartAreas[0].RecalculateAxesScale();
            }
        }

        private void checkedListBox1_Changed(object sender, ItemCheckEventArgs e)
        {
            String pName = (String)checkedListBox1.Items[e.Index];
            if (e.NewValue == CheckState.Checked)
            {

                chart1.Series.Add(pName);
                foreach (MetaTestResult mtr in group.List)
                {
                    double avarage = 0;
                    int n = 0;
                    foreach (TestResult tr in mtr.Tests)
                    {

                        Metric m = tr.getMetricByName(pName);
                        if (m != null)
                        {
                            n++;
                            avarage += m.Value;
                        }//esle ignore
                    }
                    chart1.Series[pName].Points.AddXY(mtr.TripleNumber, avarage / n);
                }
                chart1.Series[pName].ChartType = System.Windows.Forms.DataVisualization.Charting.SeriesChartType.Spline;
                chart1.Series[pName].BorderWidth = 2;
            }
            else {
                chart1.Series.Remove(chart1.Series[pName]);
            }

    
                
             
        }
 
    }
}
