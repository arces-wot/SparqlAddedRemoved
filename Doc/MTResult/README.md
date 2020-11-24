## Introduction
Test for time metrics about added and removed triples by an **UPDATE**.

Test for time metrics about the added and removed triples by an Update.
The phases of the algorithm that are considered in this report are:
- The **CONSTRUCT** execution (if needs)
- The **ASK** execution, it is not realy an ASK query but it is an SELECT which replaces the ASK <a href="https://github.com/arces-wot/SparqlAddedRemoved/blob/master/Doc/MTResult/better_ask/README.md">(see here for more info)</a>
- The InsertData Update (for the added triples)
- The DeleteData Update (for the removed triples)

Test varying on:
- **UPDATE** type
- Number of triples
- Number of graphs
- Endpoint: Virtuoso and Blazegraph

## JSAP

Jsap used for run these tests:

- 6MT_SG.jsap:	6 type of meta test on a single graph. Max triples: 512.
- 6MT_2G.jsap:	6 type of meta test on 2 different graph. Max triples: 64 for each graph (total: 128).
- 6MT_4G.jsap.	6 type of meta test on 4 different graph. Max triples: 64 for each graph (total: 256).
- 6MT_8G.jsap:	6 type of meta test on 8 different graph. Max triples: 64 for each graph (total: 512).

Each Meta-Test rappresent one type of **UPDATE**:

- UpdateDataInsert
- UpdateDataDelete
- UpdateDeleteWhere
- UpdateModify (UpdateModify with where-pattern void)
- UpdateModify2 (UpdateModify with where-pattern not void)
- UpdateModify2R (UpdateModify with where-pattern reversed respect UpdateModify2)



## Summary image of single graph tests

For the x axis the samples are 1, 2, 4, 8 ,16, 32, 64, 128, 256, 512 triples.

<div align="center">
<a href="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/SG">
  <img src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/SG.png">
</a>
</div>


## Summary image of multiple graphs tests

As number of the graphs grows, the total number of triples grows,
because the range of the number of the triples for each graph is between 2^0 and 2^6,
therefore each graph in the last Single-Test of the Meta-Test has 64 triple.
For example in the Meta-Test with 2 graphs, in the last Single-Test there are 128 total triples,
instead the last Single-Test of the 8 graphs Meta-Test has 512 triples.



<div align="center">
<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/Doc/img/MG">
  <img src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/MG.png">
</a>
</div>



## InsertData DeleteData constrcution

Ther is an additional time not considered in the previous graphics, the time for build the InsertData and the DeleteData update.

Meta-Test on Virtuoso, single graph 
<div align="center">
<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/Doc/img/InsDelConstruction">
  <img src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/InsDelConstruction.png">
</a>
</div>


Meta-Test on Virtuoso, 8 graphs 
<div align="center">
<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/Doc/img/InsDelConstruction8g">
  <img src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/InsDelConstruction8g.png">
</a>
</div>

For Blazegraph the result is approximately the same.


