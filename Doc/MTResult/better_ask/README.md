## Introduction
Sparql1.1 does not allow to concatenate multiple **ASK** in a single query, therefore to retrieve global added and removed triples result from an update query, we are forced to call N **ASK** query where N is the number of the triple of the **CONSTRUCT** result.
This is a big timing problem because it's multiply for N the HTTP overhead.

Researching for a substitution for the multiply **ASK** we have found some **SELECT** with the same goal but just one query invocation.

## TEST ENVIROMENT
The tests are performed with Sepa as middleware. Virtuoso and Balzegrah as the endpoint.

## ASK
An example of an **ASK** query on a single triple (s,p,o)
(One query for triple, HTTP overhead multiply for the number of the triples)
```sparql
ASK  { GRAPH<g> {<s> <p> <o> } }
```

## SELECT AS ASK (ASK ALTERNATIVE)

- **AsksAsSelectExplicitGraph**
	 Retrive all the triples of the explicit graph (g0) that match on the kb.
	 One query for each graph, HTTP overhead multiply for the number of the graph.

```sparql
SELECT ?s ?p ?o {
	GRAPH <g0> {
		?s ?p ?o.
	}
	VALUES (?s ?p ?o) {
		(<s0><p0><o0>)
		(<s1><p1><o1>)
	}
}
```

- **ASKsAsSelectGraphAsVar**
	 Retrieve all the triples and their graph, that match with the kb.
	 Only one query needed.

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

- **AsksAsSelectFilter**
	 Retrive all the triples of the explicit graph (g0) that match on the kb.
	 One query for each graph, HTTP overhead multiply for the number of the graph.

```sparql
SELECT ?s ?p ?o WHERE{
	GRAPH <g0>{
		?s ?p  ?o.
	}
	FILTER(
		?s = <s0> &&  ?p = <p0> && ?o = <o0> ||
		?s = <s1> &&  ?p = <p1> && ?o = <o1>
	)
}
```

- **ASKsAsSelectExistList**
	 Retrieve all the triples and their graph, that match with the kb.
	 Only one query needed.
	The variable "?i" is used to ensure to match the "?x" value with his triple. It is recommended in case the result from the endpoint will not be ordered as well.
	Note: Virtuoso return the value of "?x" as integer "1" or "0", instead of BLazegrah that return "true" or "false".

```sparql
SELECT ?x ?i {
	VALUES (?g ?s ?p ?o ?i) {
		(<g0><s0><p0><o0> 0)
		(<g1><s1><p1><o1> 1)
	}
	BIND(EXISTS{GRAPH ?g {?s ?p ?o}} AS ?x)
}
```

## The order is important

Some endpoint as Virtuoso have very different waiting time in according to the order of the query patterns.
For example taking the query **ASKsAsSelectGraphAsVar** with the GRAPH pattern before the VALUES pattern it take so long time.
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
<div align="center">
<a href="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/BA_02">
  <img src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/BA_02.png">
</a>
</div>
(test result file: "\Doc\MTResult\better_ask\virtuoso.json")

The same test with the same query **ASKsAsSelectGraphAsVar** but with the GRAPH pattern after the VALUES pattern take less time.
```sparql
SELECT ?g ?s ?p ?o {
		VALUES (?g ?s ?p ?o) {
			  (<g0><s0><p0><o0>)
			  (<g1><s1><p1><o1>)
		}		
		GRAPH ?g {
			?s ?p ?o.
		}
}
```
<div align="center">
<a href="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/BA_01">
  <img src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/BA_01.png">
</a>
</div>
(test result file: "\Doc\MTResult\better_ask\virtuoso_order_graph_value.json")

## Finial test report

<div align="center">
<a href="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/betterAsk">
  <img src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/betterAsk.jpg">
</a>
</div>


## Info

Test result file:
- "\Doc\MTResult\better_ask\blazegraph.json"
- "\Doc\MTResult\better_ask\virtuoso.json"

Jsap used for all this tests:
- "\Doc\MTResult\better_ask\6MT_SG.jsap"





