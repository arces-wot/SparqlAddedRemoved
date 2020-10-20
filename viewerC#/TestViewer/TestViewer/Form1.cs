using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
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
            metaTestView1.setTestResultView(testResultView1);
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (openFileDialog1.ShowDialog()==DialogResult.OK) {

                var myJsonString = File.ReadAllText(openFileDialog1.FileName);
                JObject myJObject = JObject.Parse(myJsonString);
                int index = 0;
                comboBox1.Items.Clear();
                foreach (JProperty item in myJObject.Children())
                {
                    MetaTestResult mtr = new MetaTestResult((JObject)myJObject[item.Name], index);
                    comboBox1.Items.Add(mtr);
                    index++;
                }
               
                labelInfo.Text = "Number of MetaTest: " + comboBox1.Items.Count;
            }

        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox1.SelectedItem!=null)
            {
                MetaTestResult selected = (MetaTestResult)comboBox1.SelectedItem;
                metaTestView1.loadMetaTest(selected);
            }
        }
    }
}
