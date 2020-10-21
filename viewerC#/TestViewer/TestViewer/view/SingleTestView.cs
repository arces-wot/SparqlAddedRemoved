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
    public partial class SingleTestView : UserControl
    {
        public SingleTestView()
        {
            InitializeComponent();
        }
        public void loadSingleTest(MetaTestResult mt) {
            labelInfo.Text = "Error count: " + mt.errorCount() + "\nWarnings count: "+ mt.warningCount();
            
            metricsTable1.loadPhases(mt.Avarages);
        }

        
    }
}
