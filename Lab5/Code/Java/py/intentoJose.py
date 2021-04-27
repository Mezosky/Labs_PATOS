from pyspark.sql import SparkSession
import sys

if __name__ == "__main__":
    '''
    para ejecutar, copiar/pegar en el server, o subir este archivo y luego:
    spark-submit --master spark://cluster-01:7077 lab05.py
    '''
    # en duro los nombres de archivo. Así es más simple de correr
    filein = "hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings-test.tsv"
    fileout = "hdfs://cm:9000/uhadoop2021/grupo34/testpy/"
    spark = SparkSession.builder.appName("Pythonlab5_grupo34").getOrCreate()
    inputRDD = spark.read.text(filein).rdd.map(lambda r: r[0])
    lines = inputRDD.map(lambda line: line.split("\t"))
    tvSeries = lines.filter(lambda line: ("TV_SERIES" == line[6]) and not ('null' == line[7]))
    # pienso que este rdd es el que hay que cachear, debido a que se utiliza 2 veces a continuacion
    #tvSeries.cache()
    # rdd con key tvserie#año, y luego ranking y nombre episodio
    seriesEpisodes = tvSeries.map(lambda line: (line[3]+ "#" + line[4], (float(line[2]), line[7])))
    # rdd con key tvserie#año, y ranking
    seriesEpisodeRating = tvSeries.map(lambda line: (line[3]+ "#" + line[4], float(line[2])))
    # se calcula max ranking
    maxRating = seriesEpisodeRating.reduceByKey(lambda maxR, rating : rating if maxR < rating else maxR)
    # se calcula avg
    seriesToSumCountRating = seriesEpisodeRating.aggregateByKey((0.0, 0), \
        lambda sumCount, rating: (sumCount[0] + rating, sumCount[1] + 1), \
        lambda sumCountA, sumCountB: (sumCountA[0] + sumCountB[0], sumCountA[1] + sumCountB[1]))
    seriesToAvgRating = seriesToSumCountRating.mapValues(lambda tup2n: tup2n[0]/tup2n[1])
    # se joinea todo
    maxAvgJoin = maxRating.join(seriesToAvgRating)
    episodeMaxAvgJoin = seriesEpisodes.join(maxAvgJoin)
    finalFilter = episodeMaxAvgJoin.filter(lambda a: a[1][0][0]==a[1][1][0])
    # debug por pantalla!
    for x in finalFilter.collect() :
        print(x)