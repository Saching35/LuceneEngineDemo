# InformationRetreivalAssignmentTwo
Uses Lucene to perform queries on the document collection

Compile and run the project using

```source index-and-query-collection.sh```

Output will be in a file called Qresult in this directory

Run TrecEval to evaluate these results

```./../trec_eval-9.0.7/trec_eval <qrel_file> Qresult```

This will output on the terminal an evaluation of the performance of the information retrieval system
