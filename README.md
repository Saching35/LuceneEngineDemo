# LuceneEngineDemo

To run the jar following command can be used:

java -jar LuceneSearchEngine.jar



Which similarity to be used can be defined in the property file present in /similarity/similarity.properties
Values for the similarities are:
1 - BM25Similarity
2 - BooleanSimilarity
6 - ClassicSimilarity


command to run trec_eval(to be run from luceneDemo/LuceneEngineDemo/):
./trec_eval '../cran/QRelsCorrectedforTRECeval' '../result/result'
