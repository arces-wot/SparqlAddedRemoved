## Meta-Test Updates


Each Meta-Test rappresent one type of **UPDATE**:

- UpdateDataInsert
- UpdateDataDelete
- UpdateDeleteWhere
- UpdateModify (UpdateModify with where-pattern void)
- UpdateModify2 (UpdateModify with where-pattern not void)
- UpdateModify2R (UpdateModify with where-pattern reversed respect UpdateModify2)


The follow sparql updates are only one graph and only one triple,
Ther are one group of MetaTest, enclosed in a JSAP file, for each test on different number of graphs (1,2,4 and 8 graphs).
The MetaTest will incrase exponentially the number of triples (where need, for example don't on UpdateDeleteWhere **UPDATE**).

**UPDATE** sparql:

- UpdateDataInsert

```sparql
PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>
INSERT DATA  {  
	GRAPH <http://lumb/for.sepa.test/workspace/defaultgraphX> { 
		<http://www.unibo.it/StudentY> ub:memberOf <http://www.unibo.it>
	}
}	
```

- UpdateDataDelete

```sparql
PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>
DELETE DATA { 
	GRAPH  <http://lumb/for.sepa.test/workspace/defaultgraphX> { 
				<http://www.unibo.it/StudentY> ub:memberOf <http://www.unibo.it>
	}
}	
```

- UpdateDeleteWhere

```sparql
PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>
DELETE WHERE {
	GRAPH <http://lumb/for.sepa.test/workspace/defaultgraphX> { 
		?s ub:memberOf <http://www.unibo.it>
	} 
}	
```

- UpdateModify

```sparql
PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>
DELETE { 
	GRAPH <http://lumb/for.sepa.test/workspace/defaultgraphX>  {
		<http://www.unibo.it/StudentY> ub:memberOf <http://www.unibo.it>
	} 
} INSERT {
	GRAPH <http://lumb/for.sepa.test/workspace/defaultgraphX> {		
		<http://www.unibo.it/GraduatedStudentY> ub:memberOf <http://www.unibo.it>
	}
} WHERE {}
```


- UpdateModify2

```sparql
DELETE {  
		GRAPH <http://lumb/for.sepa.test/workspace/defaultgraph0>  {
			?s ?p ?o
		} 
} INSERT {  
		GRAPH <http://lumb/for.sepa.test/workspace/defaultgraph0> {			
			<http://www.unibo.it/GraduatedStudentY> ub:memberOf <http://www.unibo.it>
		} 
} WHERE {
		?s ?p ?o .
		?s ub:memberOf <http://www.unibo.it>
}
```

- UpdateModify2R

```sparql
DELETE {  
		GRAPH <http://lumb/for.sepa.test/workspace/defaultgraph0>  {
			?s ?p ?o
		} 
} INSERT {  
		GRAPH <http://lumb/for.sepa.test/workspace/defaultgraph0> {			
			<http://www.unibo.it/GraduatedStudentY> ub:memberOf <http://www.unibo.it>
		} 
} WHERE {
		?s ub:memberOf <http://www.unibo.it> .
		?s ?p ?o 
}
```