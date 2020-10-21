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

        private void button1_Click(object sender, EventArgs e)
        {
            if (openFileDialog1.ShowDialog()==DialogResult.OK) {

                var myJsonString = File.ReadAllText(openFileDialog1.FileName);
                JObject myJObject = JObject.Parse(myJsonString);
                comboBox1.Items.Clear();
                Dictionary<String,MetaTestGroup> temp = new Dictionary<String, MetaTestGroup>();
                foreach (JProperty item in myJObject.Children())
                {
                    MetaTestResult mtr = new MetaTestResult((JObject)myJObject[item.Name]);
                    MetaTestGroup mtg;
                    if (temp.TryGetValue(mtr.Name, out mtg))
                    {
                        mtg.List.Add(mtr);
                        temp.Remove(mtr.Name);
                    }
                    else {
                        mtg = new MetaTestGroup(mtr.Name);
                        mtg.List.Add(mtr);
                    }
                    temp.Add(mtr.Name, mtg);
                }
                int count = 0;
                int warnings = 0;
                foreach (String name in temp.Keys) {
                    MetaTestGroup mtg;
                    if (temp.TryGetValue(name, out mtg))
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
                allMetricChart1.loadMetaTest(temp.Values.ToList<MetaTestGroup>());
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
                metaTestView1.loadSingleTest(selected);
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
    }
}
