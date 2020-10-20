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
    public partial class MetricsTable : UserControl
    {
        public MetricsTable()
        {
            InitializeComponent();
        }

        public void loadPhases(List<Metric> list)
        {

            dataGridView1.Rows.Clear();
            foreach (Metric m in list)
            {
                dataGridView1.Rows.Add(m.toObjArray());
            }
        }
    }
}
