//descrizione risultati 


#################  4MT_singleGraph.json
Risultati di construct + ask su 4 meta test utilizzando 1 grafo soltanto

#################  ResultASK_30_10_2020.json
Risultati di construct + ask su 5 meta test utilizzando 1 grafo soltanto con 32Triple:
UpdateDateDelete
UpdateDeleteWhere
UpdateDataInsert
UpdateModify
UpdateModify2

#################  AskVsSelect.json
Risultati per conparare le normali ASK con la Query Select che le sostituisce
Query: 

SELECT ?g ?s ?p ?o {
	GRAPH ?g {  
		?s ?p ?o.
	}
	VALUES (?g ?s ?p ?o) {
  		(<g><s0><p><o>)
  		(<g><s1><p><o>)
		(<g><s2><P><o>)
	}
}


#################  AskVsSelect2.json
Mette a confronto le ask e le sequenti due query in sostituzione:

SELECT ?g ?s ?p ?o {
	GRAPH ?g {  
		?s ?p ?o.
	}
	VALUES (?g ?s ?p ?o) {
  		(<g><s0><p><o>)
  		(<g><s1><p><o>)
		(<g><s2><P><o>)
	}
}

con (dove il grafo G è esplicitato e non è una variabile):

SELECT ?s ?p ?o {
GRAPH <G> {  
?s ?p ?o.
}
VALUES (?s ?p ?o) {
  (<s> <p> <o>)
  (<s1>	<P> <o1>)
  (<s2> <P> <o2>)
}
}

#################  AskVs3SelectTypes.json

stessi test di AskVsSelect2.json 
più:

SELECT  ?s ?p ?o WHERE{
GRAPH <prova3>{
?s ?p  ?o.
}
FILTER( 
?s = <s1> &&  ?p = <P> && ?o = <o1> ||
?s = <s2> &&  ?p = <P> && ?o = <o1> 
)
}

#################  AskVs4SelectTypes.json

stessi test di AskVs3SelectTypes.json
più:

SELECT ?x {
VALUES (?g ?s ?p ?o ) {
  (<prova3><s> <p> <o> )
  (<prova3><s1>	<P> <o1> )
(<prova2><s2> <P> <o2> )
(<miao><s2asd> <P> <o2> )
}
BIND(EXISTS{GRAPH ?g {?s ?p ?o}} AS ?x)
}

