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
    public partial class TestResultView : UserControl
    {
        public TestResultView()
        {
            InitializeComponent();
        }

        public void loadTestResult(TestResult tr) {
            label1.Text = tr.ToString();
            dataGridView1.Rows.Clear();
            dataGridView1.Rows.Add("Insert Delete check result", tr.UpdateSameOfInsertDelete);
            dataGridView1.Rows.Add("Ask check done", tr.AskCheckDone);
            if(tr.AskCheckDone) {
                dataGridView1.Rows.Add("Ask Delete check result", tr.AskDeleteOk);
                dataGridView1.Rows.Add("Ask Insert check result", tr.AskInsertOk);
            }
            dataGridView1.Rows.Add("Total Triple test count", tr.TestTripleCount);
            dataGridView1.Rows.Add("Pre insered Triple count", tr.PreInseredTripleCount);
            dataGridView1.Rows.Add("Pre Query Triple count", tr.PreQueryTriples_count);
            dataGridView1.Rows.Add("Ask Insert Triple count", tr.AskAddedTriples_count);
            dataGridView1.Rows.Add("Ask Delete Triple count", tr.AskRemovedTriples_count);
            dataGridView1.Rows.Add("Query Triple count after Normal Update", tr.AfterUpdateQueryTriples_count);
            dataGridView1.Rows.Add("Query Triple count after Insert Delete Update", tr.AfterInsDelQueryTriples_count);
            dataGridView1.Rows.Add("Triple example before the test", tr.PreQueryTriple_example);
            dataGridView1.Rows.Add("Triple example after Norma Update", tr.AfterUpdateQueryTriple_example);
            dataGridView1.Rows.Add("Triple example after Insert Delete Update", tr.AfterInsDelQueryTriple_example);
            dataGridView1.Rows.Add("Triple example ASK Insert", tr.AskAddedTriple_example);
            dataGridView1.Rows.Add("Triple example ASK Delete", tr.AskRemovedTriple_example);
            metricsTable1.loadPhases(tr.Phases);
        }

        private void TestResultView_Load(object sender, EventArgs e)
        {

        }
    }
}
