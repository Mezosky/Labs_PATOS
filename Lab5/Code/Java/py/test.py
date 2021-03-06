from __future__ import print_function

import sys
from pyspark.sql import SparkSession

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: average_series_rating.py <filein> <fileout>", file=sys.stderr)
        sys.exit(-1)

    filein = sys.argv[1] #"hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings-test.tsv"
    fileout = sys.argv[2] #"hdfs://cm:9000/uhadoop2021/<user>/series-avg/"

    spark = SparkSession.builder.appName("Pythonlab5").getOrCreate()

    inputRDD = spark.read.text(filein).rdd.map(lambda r: r[0])

    lines = inputRDD.map(lambda line: line.split("\t"))

    tvSeries = lines.filter(lambda line: ("TV_SERIES" == line[6]) and not ('null' == line[7]))

    seriesEpisodeRating = tvSeries.map(lambda line: (line[3]+ "#" + line[4] + "#" + line[5], line[7], float(line[2])))

    # Maximo valor
    seriesToEpisodeRating = seriesEpisodeRating.map(lambda tup: (tup[0], tup[2]))
    seriesMax = seriesToEpisodeRating.reduceByKey(max)

    # Promedio de las series
    seriesToSumCountRating = seriesToEpisodeRating.aggregateByKey((0.0, 0), \
        lambda sumCount, rating: (sumCount[0] + rating, sumCount[1] + 1), \
        lambda sumCountA, sumCountB: (sumCountA[0] + sumCountB[0], sumCountA[1] + sumCountB[1]))
    seriesToAvgRating = seriesToSumCountRating.mapValues(lambda tup2n:  round(tup2n[0]/tup2n[1], 2))

    # Juntar episodios con mejor rating
    """seriesToEpisodeRating = seriesEpisodeRating.map(lambda tup: (tup[0], tup[1]))
    seriesEpisodes = seriesToEpisodeRating.aggregateByKey((''), \
        lambda sumString, tupla: (sumString + '|' + tupla), \
        lambda sumCountA, sumCountB: (sumCountA + sumCountB))
"""
    seriesToEpisodes = seriesEpisodeRating.map(lambda tup: (tup[0], tup[1]))
    join_test = seriesMax.join(seriesToEpisodes)

    join_test = join_test.map(lambda tup: (tup[0], tup[1]))
    seriesEpisodes = join_test.aggregateByKey((''), \
        lambda sumString, tupla: (sumString + '|' + tupla[1]), \
        lambda sumCountA, sumCountB: (sumCountA + sumCountB))
    #Join
    #join_series = seriesMax.join(seriesToAvgRating)
    #join_series = join_series.flatMap()

    seriesEpisodes.saveAsTextFile(fileout);

    spark.stop()