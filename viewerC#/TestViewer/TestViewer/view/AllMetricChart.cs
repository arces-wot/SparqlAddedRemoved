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
using TestViewer.utils;

namespace TestViewer.view
{
    public partial class AllMetricChart : UserControl
    {
        private List<MetaTestGroup> groups;
        private int lineSize = 2;

        public int LineSize { get => lineSize; set => lineSize = value; }

        public AllMetricChart()
        {
            InitializeComponent();
        }

        public void loadMetaTest(List<MetaTestGroup> groups)
        {
            this.groups = groups;
            comboBox1.Items.Clear();
            chart1.Series.Clear();
            //ottengo la lista delle fasi (dando per scontato che sia la medesima per tutti i test)
            if (groups.Count > 0 
                && groups.ElementAt(0).List.Count>0 
                && groups.ElementAt(0).List.ElementAt(0).Tests.Count > 0
            )
            {
                bool askPresent = false;
                bool updatePresent = false;
                bool constructPresent = false;
                bool insDelPresent = false;
                bool allPresent = false;
                foreach (Metric m in groups.ElementAt(0).List.ElementAt(0).Tests.ElementAt(0).Phases)
                {
                    comboBox1.Items.Add(m.Name);              
                    if (m.Name== "Constructs") {
                        constructPresent = true;
                    }
                    if (m.Name == "ASKs") {
                        askPresent = true;
                    }
                    if (m.Name == "Execution insert and delete")
                    {
                        insDelPresent = true;
                    }
                    if (m.Name == "Execution normal update")
                    {
                        updatePresent = true;
                    }
                    if (m.Name == "Added removed extraction and generation of updates (insert and delete)")
                    {
                        allPresent = true;
                    }
                }
                if (askPresent && constructPresent ) {
                    if (updatePresent) {
                        List<string> mNames=new List<string>();
                        mNames.Add("Constructs");
                        mNames.Add("ASKs");
                        mNames.Add("Execution normal update");
                        comboBox1.Items.Add(new SpecialMetricSum("Construct+Ask+Update", mNames));
                    }
                    if (insDelPresent) {
                        List<string> mNames = new List<string>();
                        mNames.Add("Constructs");
                        mNames.Add("ASKs");
                        mNames.Add("Execution insert and delete");
                        comboBox1.Items.Add(new SpecialMetricSum("Construct+Ask+Insert+Delete", mNames));                       
                    }
                    if (allPresent)
                    {
                        Dictionary<string, Op> mOpDictionary = new Dictionary<string, Op>();
                        mOpDictionary.Add("Added removed extraction and generation of updates (insert and delete)", Op.ADD);
                        mOpDictionary.Add("Constructs", Op.SUB);
                        mOpDictionary.Add("ASKs", Op.SUB);
                        comboBox1.Items.Add(new SpecialMetricMath("Construction of Insert and Delete", mOpDictionary));
                    }

                }
            }
            if (comboBox1.Items.Count>=0) {
                comboBox1.SelectedIndex = 0;
            }
        }

        public void setMaxY(int max)
        {
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

        private void AllMetricChart_Load(object sender, EventArgs e)
        {

            chart1.Series.Clear();
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            plot();



        }

        private void radioButton1_CheckedChanged(object sender, EventArgs e)
        {
            plot();

        }

        public void refreshPlot() {
            plot();
        }
        private void plot() {
            if (comboBox1.SelectedItem != null)
            {
                chart1.Series.Clear();
                foreach (MetaTestGroup group in groups)
                {
                    String name = group.MetaTestName;//.Replace(" ", "_");
                    chart1.Series.Add(name);

                    foreach (MetaTestResult mtr in group.List)
                    {
                        double[] temp = new double[mtr.Tests.Count];
                        int index = 0;
                        double avarage = 0;
                        foreach (TestResult tr in mtr.Tests)
                        {
                            if (comboBox1.SelectedItem is SpecialMetric)
                            {
                               double specialMetricValue = ((SpecialMetric)comboBox1.SelectedItem).getValue(tr);                              
                                temp[index] = specialMetricValue;
                                index++;
                                avarage += specialMetricValue;
                            }
                            else {
                                Metric m = tr.getMetricByName((String)comboBox1.SelectedItem);
                                if (m != null)
                                {
                                    temp[index] = m.Value;
                                    index++;
                                    avarage += m.Value;
                                }//esle ignore
                            }
                           
                        }
                        if (radioButton1.Checked)
                        {
                            chart1.Series[name].Points.AddXY(mtr.TripleNumber, temp[index / 2]);
                        }
                        else
                        {
                            chart1.Series[name].Points.AddXY(mtr.TripleNumber, avarage / index);
                        }
                    }
                    chart1.Series[name].ChartType = System.Windows.Forms.DataVisualization.Charting.SeriesChartType.Spline;
                    chart1.Series[name].BorderWidth = lineSize;
                    chart1.Series[name].MarkerSize = 10;
                }

            }
        }
    }
}
