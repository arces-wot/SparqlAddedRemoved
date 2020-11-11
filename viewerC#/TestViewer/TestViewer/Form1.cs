using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;
using TestViewer.model;
using TestViewer.model.jsap;

namespace TestViewer
{
    public partial class Form1 : Form
    {
        private Dictionary<String, MetaTestGroup> dictionary;
        private Jsap jsap;
        private Dictionary<String, String> longName_name;
        private bool thereAreAllMetricsNeeded = false;

        public Form1()
        {
            InitializeComponent();
            dataGridView1.SelectionChanged += plotComparison;
            enableComparisonZone(false);
        }



        private void button1_Click(object sender, EventArgs e)
        {
            openFileDialog1.Filter = "json files (*.json)|*.json|All files (*.*)|*.*";
            if (openFileDialog1.ShowDialog() == DialogResult.OK) {

                try {
                    thereAreAllMetricsNeeded = true;
                    var myJsonString = File.ReadAllText(openFileDialog1.FileName);
                    JObject myJObject = JObject.Parse(myJsonString);
                    comboBox1.Items.Clear();
                    dictionary = new Dictionary<String, MetaTestGroup>();
                    foreach (JProperty item in myJObject.Children())
                    {
                        MetaTestResult mtr = new MetaTestResult((JObject)myJObject[item.Name]);
                        MetaTestGroup mtg;
                        if (dictionary.TryGetValue(mtr.Name, out mtg))
                        {
                            mtg.List.Add(mtr);
                            dictionary.Remove(mtr.Name);
                        }
                        else
                        {
                            mtg = new MetaTestGroup(mtr.Name);
                            mtg.List.Add(mtr);
                        }
                        dictionary.Add(mtr.Name, mtg);
                    }
                    int count = 0;
                    int warnings = 0;
                    foreach (String name in dictionary.Keys)
                    {
                        MetaTestGroup mtg;
                        if (dictionary.TryGetValue(name, out mtg))
                        {
                            count += mtg.errorCount();
                            warnings += mtg.warningCount();
                            comboBox1.Items.Add(mtg);
                        }
                    }
                    if (comboBox1.Items.Count > 0)
                    {
                        comboBox1.SelectedIndex = 0;
                    }
                    labelInfo.Text = "Number of MetaTest: " + comboBox1.Items.Count
                        + "\nError count: " + count + "\nWarnings count: " + warnings;
                    labelLoadedTest.Text = "Loaded: " + openFileDialog1.SafeFileName;
                    allMetricChart1.loadMetaTest(dictionary.Values.ToList<MetaTestGroup>());
                    ipotesi1(dictionary);
                }
                catch (Exception ex) {
                    Console.WriteLine("Not valid json file: " + ex.Message);
                    MessageBox.Show("Not valid json file.");
                    
                }

            }


        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {

            if (comboBox1.SelectedItem != null)
            {
                MetaTestGroup selected = (MetaTestGroup)comboBox1.SelectedItem;
                comboBox2.Items.Clear();
                comboBox2.Items.AddRange(selected.List.ToArray());
                if (comboBox2.Items.Count > 0) {
                    comboBox2.SelectedIndex = comboBox2.Items.Count-1;
                }
                label3.Text = "MetaTest error: " + selected.errorCount() + "\nWarnings count: " + selected.warningCount();
                metaChart1.loadMetaTest(selected);
                sparqlView1.load(selected.MetaTestName);
                label4.Text = "["+selected.MetaTestName + "] Select execution:";
                label2.Text ="[" +selected.MetaTestName+ "] Select Single test of MetaTest:";

            }
        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox1.SelectedItem != null)
            {
                MetaTestResult selected = (MetaTestResult)comboBox2.SelectedItem;
                singleTestView1.loadSingleTest(selected);
                comboBox3.Items.Clear();
                comboBox3.Items.AddRange(selected.Tests.ToArray());
                if (comboBox3.Items.Count > 0)
                {
                    comboBox3.SelectedIndex = comboBox3.Items.Count-1;
                }
            }
        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void comboBox3_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox3.SelectedItem != null)
            {
                TestResult selected = (TestResult)comboBox3.SelectedItem;
                testResultView1.loadTestResult(selected);
            }
        }

        private void radioButton1_CheckedChanged(object sender, EventArgs e)
        {
            ipotesi1(dictionary);
            plotComparison(sender, e);
        }

        private void ipotesi1(Dictionary<String, MetaTestGroup> metaTests)
        {
            dataGridView1.Rows.Clear();
            longName_name = new Dictionary<String, String>();     
            foreach (String name in metaTests.Keys) {
                MetaTestGroup mtg;
                if (metaTests.TryGetValue(name, out mtg)) {


                    foreach (MetaTestResult mtr in mtg.List)
                    {
                        double[] update = new double[mtr.Tests.Count];
                        double[] construct = new double[mtr.Tests.Count];
                        double[] asks = new double[mtr.Tests.Count];
                        double[] insertDelete = new double[mtr.Tests.Count];
                        int index = 0;
                        foreach (TestResult tr in mtr.Tests)
                        {
                            try {
                                construct[index] = tr.getMetricByName("Constructs").Value;
                                asks[index] = tr.getMetricByName("ASKs").Value;
                                insertDelete[index] = tr.getMetricByName("Execution insert and delete").Value;
                                update[index] = tr.getMetricByName("Execution normal update").Value;
                                index++;
                            }
                            catch (Exception) {
                                thereAreAllMetricsNeeded = false;
                                break;
                            }                        
                        }
                        if (thereAreAllMetricsNeeded)
                        {
                            if (radioButton1.Checked)
                            {
                                int med = index / 2;
                                double temp = construct[med] + asks[med] + insertDelete[med];
                                double temp2 = construct[med] + asks[med] + update[med];
                                dataGridView1.Rows.Add(mtr.Name + "_T" + mtr.TripleNumber, update[med], temp, temp2);
                            }
                            else
                            {
                                double cai = 0;
                                double up = 0;
                                double up2 = 0;
                                for (int x = 0; x < index; x++)
                                {
                                    up += update[x];
                                    cai += construct[x] + asks[x] + insertDelete[x];
                                    up2 += construct[x] + asks[x] + update[x];
                                }
                                dataGridView1.Rows.Add(mtr.Name + "_T" + mtr.TripleNumber, up / index, cai / index, up2 / index);
                            }
                            longName_name.Add(mtr.Name + "_T" + mtr.TripleNumber, mtr.Name);
                        }
                        else {
                            break;
                        }
                      
                    }
                    if (!thereAreAllMetricsNeeded)
                    {
                        break;
                    }

                }//else ignore

            }
            enableComparisonZone(thereAreAllMetricsNeeded);
        }

        private void button2_Click(object sender, EventArgs e)
        {
            openFileDialog1.Filter = "jsap files (*.jsap)|*.jsap|All files (*.*)|*.*";
            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            { 
                var myJsonString = File.ReadAllText(openFileDialog1.FileName);
                JObject myJObject = JObject.Parse(myJsonString);
                jsap = new Jsap(myJObject);
                labelLoadedJsap.Text = "Loaded: " + openFileDialog1.SafeFileName;
                sparqlView1.load(jsap);
            }
        }


        private void enableComparisonZone(bool enable) {
            labelUnavailable.Visible = !enable;
            if (!enable)
            {
                dataGridView1.Rows.Clear();
                longName_name = new Dictionary<String, String>();
                labelUnavailable.Dock = DockStyle.Fill;
            }

        }


        private void checkBox2_CheckedChanged(object sender, EventArgs e)
        {
            plotComparison(sender, e);
        }

        private void checkBox3_CheckedChanged(object sender, EventArgs e)
        {
            plotComparison(sender, e);
        }

        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {
            plotComparison(sender, e);
        }

        private void plotComparison(object sender, EventArgs e) {
            if (thereAreAllMetricsNeeded) {
                String upSerie = "S3";
                String caInsdelSerie = "S1";
                String caUpSerie = "S2";
                if (dataGridView1.SelectedRows.Count > 0 && dataGridView1.SelectedRows[0].Index >= 0)
                {
                    String testName;
                    if (longName_name.TryGetValue(((String)dataGridView1.SelectedRows[0].Cells[0].Value), out testName))
                    {
                        MetaTestGroup mtg;
                        if (dictionary.TryGetValue(testName, out mtg))
                        {

                            chart1.Visible = true;
                            chart1.Series.Clear();
                            if (checkBox1.Checked)
                            {
                                chart1.Series.Add(upSerie);
                                chart1.Series[upSerie].ChartType = System.Windows.Forms.DataVisualization.Charting.SeriesChartType.Spline;
                                chart1.Series[upSerie].BorderWidth = 2;
                            }
                            if (checkBox2.Checked)
                            {
                                chart1.Series.Add(caInsdelSerie);
                                chart1.Series[caInsdelSerie].ChartType = System.Windows.Forms.DataVisualization.Charting.SeriesChartType.Spline;
                                chart1.Series[caInsdelSerie].BorderWidth = 2;
                            }
                            if (checkBox3.Checked)
                            {
                                chart1.Series.Add(caUpSerie);
                                chart1.Series[caUpSerie].ChartType = System.Windows.Forms.DataVisualization.Charting.SeriesChartType.Spline;
                                chart1.Series[caUpSerie].BorderWidth = 2;
                            }
                            foreach (MetaTestResult mtr in mtg.List)
                            {
                                double[] update = new double[mtr.Tests.Count];
                                double[] construct = new double[mtr.Tests.Count];
                                double[] asks = new double[mtr.Tests.Count];
                                double[] insertDelete = new double[mtr.Tests.Count];
                                int index = 0;
                                foreach (TestResult tr in mtr.Tests)
                                {

                                    construct[index] = tr.getMetricByName("Constructs").Value;
                                    asks[index] = tr.getMetricByName("ASKs").Value;
                                    insertDelete[index] = tr.getMetricByName("Execution insert and delete").Value;
                                    update[index] = tr.getMetricByName("Execution normal update").Value;
                                    index++;
                                }
                                double[] ris = new double[3];
                                if (radioButton1.Checked)
                                {
                                    int med = index / 2;
                                    ris[0] = update[med];
                                    ris[1] = construct[med] + asks[med] + insertDelete[med];
                                    ris[2] = construct[med] + asks[med] + update[med];
                                }
                                else
                                {
                                    double cai = 0;
                                    double up = 0;
                                    double up2 = 0;
                                    for (int x = 0; x < index; x++)
                                    {
                                        up += update[x];
                                        cai += construct[x] + asks[x] + insertDelete[x];
                                        up2 += construct[x] + asks[x] + update[x];
                                    }
                                    ris[0] = up / index;
                                    ris[1] = cai / index;
                                    ris[2] = up2 / index;
                                }

                                if (checkBox1.Checked)
                                {
                                    chart1.Series[upSerie].Points.AddXY(mtr.TripleNumber, ris[0]);
                                }
                                if (checkBox2.Checked)
                                {
                                    chart1.Series[caInsdelSerie].Points.AddXY(mtr.TripleNumber, ris[1]);
                                }
                                if (checkBox3.Checked)
                                {
                                    chart1.Series[caUpSerie].Points.AddXY(mtr.TripleNumber, ris[2]);
                                }
                            }

                        }//else ignore

                    }

                }
            }
            enableComparisonZone(thereAreAllMetricsNeeded);
        }

        private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
        { 
            if(e.RowIndex>=0){
               dataGridView1.Rows[e.RowIndex].Selected = true;
            }
         
        }
    }
}
