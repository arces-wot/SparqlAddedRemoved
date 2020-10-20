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
    public partial class MetaTestView : UserControl
    {
        private TestResultView trv;
        public MetaTestView()
        {
            InitializeComponent();
        }
        public void loadMetaTest(MetaTestResult mt) {
            labelInfo.Text = "Number of triple used for this test: " + mt.TripleNumber +
                "\nNumber of esecution of the same single test: " + mt.Replication +
                "\nNumber of single tests: " + mt.Tests.Count;
                ;
            comboBox1.Items.Clear();
            comboBox1.Items.AddRange(mt.Tests.ToArray());
            metricsTable1.loadPhases(mt.Avarages);
        }
        public void setTestResultView(TestResultView trv) { this.trv = trv; }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox1.SelectedItem != null)
            {
                TestResult selected = (TestResult)comboBox1.SelectedItem;
                this.trv.loadTestResult(selected);
            }
        }
    }
}
