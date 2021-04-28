'''
Por Ignacio Meza de la Jara y José Espina
https://github.com/Mezosky/Labs_PATOS/tree/main/Lab5

(2) the output tuple for the series "The Simpsons#1989" and another output tuple of your choice:
(u'The Simpsons#1989', u"Homer's Enemy (#8.23)", 9.2, 7.38)
Para "Twin Peaks#1990":
(u'Twin Peaks#1990', u'(#2.7)', 9.4, 8.52)
'''

from pyspark.sql import SparkSession
import sys

if __name__ == "__main__":
    '''
    para ejecutar, copiar/pegar en el server, o subir este archivo y luego:
    spark-submit --master spark://cluster-01:7077 lab05.py
    '''
    filein = sys.argv[1] #"hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings-test.tsv"
    fileout = sys.argv[2] #"hdfs://cm:9000/uhadoop2021/<user>/series-avg/"

    spark = SparkSession.builder.appName("Pythonlab5_grupo34").getOrCreate()
    inputRDD = spark.read.text(filein).rdd.map(lambda r: r[0])
    lines = inputRDD.map(lambda line: line.split("\t"))
    tvSeries = lines.filter(lambda line: ("TV_SERIES" == line[6]) and not ('null' == line[7]))
    tvSeries.cache()
    
    seriesEpisodes = tvSeries.map(lambda line: (line[3]+ "#" + line[4], (float(line[2]), line[7])))
    
    seriesEpisodeRating = tvSeries.map(lambda line: (line[3]+ "#" + line[4], float(line[2])))
    
    maxRating = seriesEpisodeRating.reduceByKey(lambda maxR, rating : rating if maxR < rating else maxR)
    
    seriesToSumCountRating = seriesEpisodeRating.aggregateByKey((0.0, 0), \
        lambda sumCount, rating: (sumCount[0] + rating, sumCount[1] + 1), \
        lambda sumCountA, sumCountB: (sumCountA[0] + sumCountB[0], sumCountA[1] + sumCountB[1]))
    seriesToAvgRating = seriesToSumCountRating.mapValues(lambda tup2n: round(tup2n[0]/tup2n[1], 2))
    
    maxAvgJoin = maxRating.join(seriesToAvgRating)
    episodeMaxAvgJoin = seriesEpisodes.join(maxAvgJoin)
    finalFilter = episodeMaxAvgJoin.filter(lambda a: a[1][0][0]==a[1][1][0])

    episodesTogether = finalFilter.map(lambda line: (line[0], line[1][0][1]))
    episodesTogether = episodesTogether.aggregateByKey((''), \
        lambda sumString, tupla: (sumString + tupla if sumString=='' else sumString + '|' + tupla), \
        lambda sumCountA, sumCountB: (sumCountA + sumCountB))

    finalMap = finalFilter.map(lambda line: (line[0], (line[1][1][0],line[1][1][1])))
    finalMap = finalMap.distinct()
    finalMap = episodesTogether.join(finalMap)
    finalMap = finalMap.map(lambda line: (line[0], line[1][0], line[1][1][0], line[1][1][1]))

    finalMap.saveAsTextFile(fileout);

    spark.stop()