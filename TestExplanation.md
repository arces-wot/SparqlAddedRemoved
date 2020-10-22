## Preambolo
I test servono a studiare i tempi e gli effetti dell'estrazione delle triple implicate in una richiesta di **UPDATE**,
tramite le query di tipo CONSTRUCT e ASK.
Parte del test, una volta ottenute le triple aggiunte e rimosse della **UPDATE** in oggetto, consiste sel verificare se
una richiesta di INSERT e una richiesta di DELETE di tali triple abbiano gli stessi effetti delle **UPDATE**, sempre raccogliendone
le tempistiche di esecuzione.

A tale scopo un test ha obbligatoriamente:
-**UPDATE**: oggetto di studio.
-QUERY: relativa query alla **UPDATE**.
-ROOLBACK: relativa roolback delle **UPDATE** (update che annulla gli effetti della **UPDATE** in analisi).

Opzionali sono invece:
-UpdatePreparation: update di preparazione al test (eseguita prima del test).
-RoolbackPreparation: update che annulla la PreUpdate (eseguita alla fine del test).

## Struttura del test
- [MetaTest](#MetaTest)
- [SingleTest](#SingleTest)
- [SingleExecution](#SingleExecution)
- [Phase](#Phase)

## MetaTest
Un MetaTest, è un test che verrà eseguito una o più volte con la possibilità di importare il numero di Triple del test.

```java
MetaTest.setPot(X);
```
Imposta il numero di volte di esecuzioni del test pari ad X,
dove il primo test verrà eseguito con una sola tripla e l'ultimo test con 2^X triple.
Se X=5 verrà eseguito il test 5 volte rispettivamente con 1,2,4,8,16 e 32 triple.

```java
MetaTest.setReiteration(Y);
```
Imposta il numero di volte che ogni singolo test deve essere rieseguito.

Se X=3 ed Y=2 si otterranno 3 SingleTest(#SingleTest) ciascuno con 2 SingleExecution(#SingleExecution)

## SingleTest
Un SingleTest racchiude le esecuzioni del medesimo test e la media dei risultati di tali esecuzioni.

## SingleExecution
La singola esecuzione di un test è divisa in 11 Phase(#Phase)

Phase1) Preparation (opzionale)
  Consiste in una Update iniziale per preparare la base di dati al test (UpdatePreparation).

Phase2) Construct
  Esecuzione della Construct estratta dalla **Update** del test.
  
Phase3) ASK
  Esecuzione delle Ask relative ai risultati dalla Construct.  
  
Phase4) Pre query
  Esecuzione della QUERY.
  
Phase5) Insert e Delete
  Esecuzione elle update di Insert e Delete generate tramite la Construct e le Ask, con quindi effetto analogo alla **UPDATE**
  
Phase6) Query
  Esecuzione della QUERY, per ottenere i cambiamenti sulla base di dati causati dalle update di Insert e Delete.
  
Phase7) Roolback
  Esecuzione della ROOLBACK, per annullare i cambiamenti sulla base di dati causati dalle update di Insert e Delete.
  
Phase8) Update
  Esecuzione della **Update**.
  
Phase9) Query
  Esecuzione della QUERY, per ottenere i cambiamenti sulla base di dati causati della **Update**.
  
Phase10) Roolback
  Esecuzione della ROOLBACK, per annullare i cambiamenti sulla base di dati causati della **Update**.
  
Phase11) Roolback Preparation (opzionale)
  Esecuzione della RoolbackPreparation, per annullare i cambiamenti sulla base di dati causati della UpdatePreparation (se eseguita).
  
La SingleExecution possiede:
1. InsertDellChackResult: flag di controllo 'true' se la **Update** e le update di Insert e Delete hanno avuto il medesimo effetto sulla base di dati.
2. AskCheckDone: flag 'true' se i controlli sulle Ask sono stati eseguiti.
3. AskDeleteCheckResult: flag 'true' se il controllo sulle ask per le triple inserite ha avuto successo.
4. AskInsertCheckResult: flag 'true' se il controllo sulle ask per le triple rimosse per le triple rimosse  avuto successo.
5. TotalTripleTestCount: numero di triple usate nel test.
6. PreInseredTripleCount: numero di triple inserite prima del test, Phase1.
7. PreQueryTripleCount: numero di triple restituite da QUERY nella Phase4.
8. AskInsertTripleCount: numero di triple da inserire secondo la construct, dopo il filtro delle ask.
9. AskDeleteTripleCount: numero di triple da rimuovere secondo la construct, dopo il filtro delle ask.
10. QueryTripleCountAfterInsertDelete: numero di triple ottenute nella Phase9.
11. QueryTripleCountAfterNormalUpdate: numero di triple ottenute nella Phase6.
12. TripleExampleBeforeTest: una tripla di esempio uttenuta nella Phase4.
13. TripleExampleAfterInsertDelete: una tripla di esempio uttenuta nella Phase9.
14. TripleExampleAfterNormalUpdate: una tripla di esempio uttenuta nella Phase6.
15. TripleExampleAskInsert: una tripla di esempio filtrata dalle ask sulle triple da inserire.
16. TripleExampleAskDelete: una tripla di esempio filtrata dalle ask sulle triple da rimuovere.

## Phase
Rappresenta una fase del singolo test (elencate in SingleExecution(#SingleExecution)).
Possiede la metric aprincipale dei test, la quantità di tempo necessaria ad eseguire la fase stessa ed un flag di errore, 'true' se l'esecuzione della fase è fallita.

## TestViewer

-Tab General
Mostra il numero di MetaTest, il numero di fasi la cui esecuzione è fallita (Error count) e il numero di flag di verifica falliti (warnings count).
Si può visualizzare il grafico di una delle fasi di tutti i MetaTest.
<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/img/img1">
  <img width="300px" src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/img/img1.png">
</a>

-Tab MetaTest
Si può visualizzare il grafico di una o più fasi per tutti i SingleTest del MetaTest selezionato.
<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/img/img2">
  <img width="300px" src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/img/img2.png">
</a>

-Tab SingleTest
Si possono visualizzare i dati medi delle varie fasi.
'Prepare Insert' indica la percentuale di dati pre inseriti nella fase Phase1 sul totale delle triple trattate dal test.
<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/img/img3">
  <img width="300px" src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/img/img3.png">
</a>


-Tab SingleExecution
Si possono visualizzare i dati relativi alle fasi di una singola esecuzione e i relativi flag.
<a href="https://github.com/FerrariAndrea/SparqlAddedRemoved/edit/master/img/img4">
  <img width="300px" src="https://raw.githubusercontent.com/FerrariAndrea/SparqlAddedRemoved/master/img/img4.png">
</a>
