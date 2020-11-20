## Introduction
Alla ricerca di una query che sostituisca la **ASK**.
Dato che gli sparql1.1 non prevede la possibilita di concatenare più **ASK**,
è necessario eseguire una query di tipo **ASK** per ogni tripla, il che è molto costoso.
Questi test mostrano i risultati di varie query che cercano di ottenere lo stesso risultato di una catena
di ASK, ma in un unica query.

## Result

Tralasciando le alternative alle ASK che impiegano svariati secondi sia utilizzando Virtuoso che Blazegrah come endpoint.
Prendiamo in considerazione:
- la canonica serie di **ASK**:

```sparql
ASK  { GRAPH<g> {<s> <p> <o> } }
```
(una richiesta per tripla)

- una select che utilizza la FILTER **ASKsAsSelectGraphAsVar**

```sparql
SELECT ?g ?s ?p ?o {
		GRAPH ?g {  
			?s ?p ?o.
		}
		VALUES (?g ?s ?p ?o) {
			  (<g0><s0><p0><o0>)
			  (<g1><s1><p1><o1>)
		}
} 
```
(una sola richiesta per tutte le triple)

- una select che utilizza la EXISTS e la BIND  **ASKsAsSelectExistList**

```sparql
SELECT ?x {
			VALUES (?g ?s ?p ?o ) {
				(<g0><s0><p0><o0>)
				(<g1><s1><p1><o1>)
			}
			BIND(EXISTS{GRAPH ?g {?s ?p ?o}} AS ?x)
}
```
(una sola richiesta per tutte le triple)


<div align="center">
<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/img/betterAsk">
  <img src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/img/betterAsk.jpg">
</a>
</div>


## Info

I risultati completi dei test qui illustrati:
"\MTResult\better_ask\ultimate_test_blazegraph.json"
"\MTResult\better_ask\ultimate_test_virtuoso.json"

Per replicare i test usare "\MTResult\better_ask\6MT_SG.jsap"





