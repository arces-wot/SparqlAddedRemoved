## Introduction
Test per controllare le tempistiche dell'algoritmo di estrazione delle triple da una **UPDATE**.
Tale algoritmo è composto da un'analisi della update, estrazione delle triple che l'update punta a modificare
se necessario tramite una query di tipo **CONSTRUCT**, seguita da una query che simula il comportamento di una **ASK** per ogni tripla,
in modo da ottenere le reali triple coinvolte.
Alla fine l'esecuzione della stessa **UPDATE** o delle sue sostitute **INSERT DATA** e **DELETE DATA**, costruite a partire dalle triple estratte.

Test effettuati variando tipologia di **UPDATE**, numero di triple e numero grafi.

## JSAP

File JSAP dei test effettuati:

-6MT_SG.jsap ->	6 tipi di meta test su singolo grafo. Triple massime: 512.
-6MT_2G.jsap ->	6 tipi di meta test su 2 grafi distinti. Triple massime: 64 per grafo (128).
-6MT_4G.jsap ->	6 tipi di meta test su 4 grafi distinti. Triple massime: 64 per grafo (256).
-6MT_8G.jsap ->	6 tipi di meta test su 8 grafi distinti. Triple massime: 64 per grafo (512).

Ogni Meta Test rappresenta un tipo di **UPDATE** diversa:

-UpdateDataInsert
-UpdateModify
-UpdateDataDelete
-UpdateDeleteWhere
-UpdateModify2
-UpdateModify2R

Ogni test è stato effettuato (tramite POST) su due endpoint: Virtuoso e Blazegraph