using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TestViewer.model.jsap
{
    interface IJsapRequest
    {
        String getSparql();
        String getNotResolvedSparql();
        List<JsapBind> getBinds();
    }
}
