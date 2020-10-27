using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using TestViewer.model.jsap;

namespace TestViewer.view
{
    public partial class SparqlView : UserControl
    {

        List<String> phases;
        Jsap jsap=null;
        String testName = null;

        public SparqlView()
        {
            InitializeComponent();
        }

        public void load(Jsap jsap) {
            this.jsap = jsap;
            comboBox1.Items.Clear();
            comboBox1.Items.AddRange(this.jsap.getSparqlPhasesName().ToArray());
        }
        public void load(String testName)
        {
            this.testName = testName;
            if (comboBox1.Items.Count>0 ) {
                comboBox1.SelectedIndex = 0;
            }
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (this.jsap != null ) {
                richTextBox1.Text = this.jsap.getSparqlOf(this.testName, (String)comboBox1.SelectedItem).Trim();
            }
            else {

                richTextBox1.Text ="Warning, JSAP not loaded.";
            }

        }
    }
}
