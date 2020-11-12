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


#################  AskVsBestSelect_5MT_16T.json
solo ask confrontata con la best solution che dove sostituire le ask

#################  FinalResultOnSingleGraph.json
test con la best solution in sostituzione alle ask
per confrontarla con l'update


################ Select1VsSelect4_Blazegraph.json
test per confrontare anche su Blazegraph le due tipologie (più gettonate) di Select che sostituiscono la ask.
(Select1 e Select 4 su Virtuoso si possono vedere dal test:AskVs4SelectTypes.json)
Risulta dunque che la differenza netta tra le due è solo su Virtuoso (dal test AskVsSelect.json) possiamo notare che
su virtuoso questa tipologia di Select1 impiega svariati secondi, invece su Blazegraph impiega decine di ms
risulta comunque più veloce la Select4


################ 5MT_Select4_T512_blazegraph.json
test su blazegraph (tramite post) con 512Triple su tutti e 5 i meta test su singolo grafo, usando la tipologia di ask "SelectExistsList"

################ output.blazegraph ed output.virtuoso.json
due test identici effettuati su virtuoso e su blazegraph per confrontare i due entpoint,
differenze tra i due: alcuni tipi di AsksAsSelect funzionano bene su blazegraph (<50ms) quando su virtuoso no (>3s)
vi sono dettagli sulla sintassi di sparql che danno fastidio a blazegraph e non a virtuoso e viceversa
(ancora non è stata fixata la post su virtuoso, dunque i test hanno poche triple)
