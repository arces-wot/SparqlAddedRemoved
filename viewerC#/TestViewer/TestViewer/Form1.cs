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

namespace TestViewer
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }
        Dictionary<String, MetaTestGroup> dictionary;

        private void button1_Click(object sender, EventArgs e)
        {
            if (openFileDialog1.ShowDialog()==DialogResult.OK) {

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
                    else {
                        mtg = new MetaTestGroup(mtr.Name);
                        mtg.List.Add(mtr);
                    }
                    dictionary.Add(mtr.Name, mtg);
                }
                int count = 0;
                int warnings = 0;
                foreach (String name in dictionary.Keys) {
                    MetaTestGroup mtg;
                    if (dictionary.TryGetValue(name, out mtg))
                    {
                        count+=mtg.errorCount();
                        warnings += mtg.warningCount();
                        comboBox1.Items.Add(mtg);
                    }
                }
                if (comboBox1.Items.Count > 0)
                {
                    comboBox1.SelectedIndex = 0;
                }
                labelInfo.Text = "Number of MetaTest: " + comboBox1.Items.Count
                    +"\nError count: "+ count + "\nWarnings count: " + warnings;
                labelLoaded.Text = "Loaded: "+ openFileDialog1.SafeFileName;
                allMetricChart1.loadMetaTest(dictionary.Values.ToList<MetaTestGroup>());
                ipotesi1(dictionary);
            }


        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            
            if (comboBox1.SelectedItem!=null)
            {
                MetaTestGroup selected = (MetaTestGroup)comboBox1.SelectedItem;
                comboBox2.Items.Clear();
                comboBox2.Items.AddRange(selected.List.ToArray());
                if (comboBox2.Items.Count>0) {
                    comboBox2.SelectedIndex = 0;
                }
                label3.Text = "MetaTest error: "+selected.errorCount() + "\nWarnings count: " + selected.warningCount();
                metaChart1.loadMetaTest(selected);
               
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
                    comboBox3.SelectedIndex = 0;
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
        }

        private void ipotesi1(Dictionary<String, MetaTestGroup> metaTests)
        {
            dataGridView1.Rows.Clear();
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

                            construct[index]= tr.getMetricByName("Constructs").Value;
                            asks[index] = tr.getMetricByName("ASKs").Value;
                            insertDelete[index] = tr.getMetricByName("Execution insert and delete").Value;
                            update[index] = tr.getMetricByName("Execution normal update").Value;
                            index++;
                        }
                        if (radioButton1.Checked)
                        {
                            int med = index / 2;
                            double temp = construct[med] + asks[med] + insertDelete[med];
                            double temp2 = construct[med] + asks[med] + update[med];
                            dataGridView1.Rows.Add(mtr.Name + "_T"+mtr.TripleNumber, update[med], temp, temp2);
                        }
                        else
                        {
                            double cai = 0;
                            double up = 0;
                            double up2 = 0;
                            for (int x=0;x< index; x++) {
                                up += update[x];
                                cai += construct[x] + asks[x] + insertDelete[x];
                                up2+= construct[x] + asks[x] + update[x];
                            }
                            dataGridView1.Rows.Add(mtr.Name + "_T" + mtr.TripleNumber,  up / index, cai / index, up2/index);
                        }
                    }

                }//else ignore

            }

        }
    }
}
