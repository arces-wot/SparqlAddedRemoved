## Introduction

This test viewer is able to read and show a json output from the added-removed tester, but it allows to read any similar test result, the only restriction is the json structure.
The added-removed tester is based on **Meta-Test**, which is a description for a set of similar tests.

# Meta-Test
The **Meta-Test** is the model for a test that will be executed one or more times with an increasing load of triples.
With **Meta-Test** we can define a base test that can be changed at run-time through descriptive data read from a jsap file. 
Each test generated from the **Meta-Test** is named **Single-Test**.

For example, to execute the same test with 1,2,3,4 and then 16 triples need to set the "pot" of a **Meta-Test** to 5, and there will be these 5 **Single-Test**

```java
MetaTest.setPot(5);
```

## Single-Test
A **Single-Test** encloses the executions of the same test with the same settings, this is useful for re-execution the same test to eliminate a various type of error as JVM lag or unusual network congestion, and then it will possible to get an average or a median of these test results.

For re-execution 3 times the same test, change the "reiteration" setting.

```java
MetaTest.setReiteration(3);
```
Each execution in the **Single-Test** is named **Single-Execution**.

##Single-Execution

The **Single-Execution** of a test is divided into **Phases**, each phase is a step of the test of which we want to measure the execution time, that measure is our main metric.
The **Phases** contains the time metric and an error indicator, which is true if there was an issue on this step of the test.
The **Single-Execution** has other indicators as counters or validation flags.

It is not mandatory to use our specific Phases.
Possible phases list:

Phase1) Preparation
  It consists of an initial Update to prepare the database for the test (UpdatePreparation).

Phase2)Added removed triples extraction and generation of **Insert Delete Update**
	Extraction, build, and execution of the Construct from the Update of the test and Ask from the Construct result.
Phase2.1) Construct  
	Execution of the **Construct** query.  
Phase2.2) ASKs
	Execution of the **Ask** query.
  
Phase3) Pre-Query
	Relative test **Query** execution, to get the kb state before the update or the analogous insert-delete.
    
Phase4) Execution insert and delete
	**Insert Delete Update** execution, with the analogous effect of the **Update**
  
Phase6) Query
	**Query** execution, to get the **Insert Delete Update** effects.
  
Phase7) Rollback
	**Rollback** execution to undo the  **Insert Delete Update** effects.
  
Phase8) Update
	**Update** execution.
  
Phase9) Query
	**Query** execution, to get the **Update** effects.
  
Phase10) Rollback
	**Rollback** execution to undo the  **Update** effects.
 
Phase11) Rollback Preparation
	RollbackPreparation execution to undo the Preparation effects.


**Single-Execution** indicators list:
1. InsertDellChackResult: flag which is 'true' if the **Update** and the **Insert Delete Update** had the same effect on the kb.
2. AskCheckDone: flag which is 'true' if the **Ask** validations are all passed.
3. AskDeleteCheckResult: flag which is 'true' if the **Ask** validation on removed triples is passed.
4. AskInsertCheckResult: flag which is 'true' if the **Ask** validation on added triples is passed.
5. TotalTripleTestCount: total triples count of the **Single-Execution**.
6. PreInseredTripleCount: insered triples count on Phase1.
7. PreQueryTripleCount: retrieved triples count on Phase4.
8. AskInsertTripleCount: count of triples that need to insert with **Insert Delete Update**.
9. AskDeleteTripleCount: count of triples that need to remove with **Insert Delete Update**.
10. QueryTripleCountAfterInsertDelete: retrieved  triples count on Phase9.
11. QueryTripleCountAfterNormalUpdate: retrieved  triples count on Phase6.
12. TripleExampleBeforeTest: an example of triple retrieved triples count on Phase4.
13. TripleExampleAfterInsertDelete: an example of triple retrieved  triples count on Phase9.
14. TripleExampleAfterNormalUpdate: an example of triple retrieved  triples count on Phase6.
15. TripleExampleAskInsert: an example of triple that needs to insert with **Insert Delete Update**.
16. TripleExampleAskDelete: an example of triple that needs to delete with **Insert Delete Update**.

## Json test result main structure

The first json level is a json-array, each element is a **Single-Test** result, referred to specific **Meta-Test**, which has:
- NAME_MT, the **Meta-Test** named
- TRIPLE_NUMBER, the triples number of the **Single-Test**
- REPLICATION, the number of re-execution of the test
- PREPARE, the percentage of pre-inserted triples among the TRIPLE_NUMBER of the test
- TESTS, the json-array of the re-execution results, of which the single element contains the json-array of the phase metrics and the list of the indicators.
 
## TestViewer


The TestViewer offer the possibility to load a json result test and for see the sparql of the test the JSAP file.

-General Tab
After loading a json test result, in the General Tab, to the left there are:
- "Number of Meta Test": the number of **Meta-Test** contained in that result
- "Error Count": the count of the failed phases occurred
- "Warnings count": the number of a not passed validation referred to each **Single-Execution**

In this tab it's possible to plot a graph representing the median or the average of a phase for each **Meta-Test**.


<div align="center">
	<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/img/img1">
	  <img width="300px" src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/Doc/img/img1.png">
	</a>
</div>


-MetaTest Tab
Selected a **Meta-Test**, it is possible to see the "Error Count" and the "Warnings count" referred to this one only.
it is possible to see sparql of all query and update of a selected **Meta-Test** with just one triple as an exemple.
it is possible to plot a graph of selected phases of the **Meta-Test** (average of all re-execution), in the x-axis there are the **Single-test**  triple counts.

 
<div align="center">
<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/img/img2">
  <img width="300px" src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/img2.png">
</a>  
</div>

-SingleTest Tab
Here you can select a **Single-Test** result, which is referred to selected **Meta-Test** in the MetaTest Tab, and get an overview of the test.

<div align="center">
<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/img/img3">
  <img width="300px" src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/img3.png">
</a>
</div>

-SingleExecution Tab
In this tab there are all phases metric and indicator of a **Single-Execution** of the previous selected **Single-Test**.


-Comparison Tab

This is a special tab, it is available only if the json test result has all the phases needed as "Construct", "ASKs", "Update" and "Execution insert and delete".
This tab allows to study the time metrics of the more important phases, for compare the **Update** with the **Insert Delete Update**, and for evaluating the ask and construct query for retrieving the added and the removed triples form an **Update**.


<div align="center">
<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/img/img4">
  <img width="300px" src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/Doc/img/img4.png">
</a>
</div>
