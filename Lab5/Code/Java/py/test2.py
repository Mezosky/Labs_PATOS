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

    seriesEpisodeRating = tvSeries.map(lambda line: (line[3]+ "#" + line[4], line[7], float(line[2])))

    # Maximo valor
    seriesToEpisodeRating = seriesEpisodeRating.map(lambda tup: (tup[0], tup[2]))
    seriesMax = seriesToEpisodeRating.reduceByKey(max)

    # Promedio de las series
    seriesToSumCountRating = seriesToEpisodeRating.aggregateByKey((0.0, 0), \
        lambda sumCount, rating: (sumCount[0] + rating, sumCount[1] + 1), \
        lambda sumCountA, sumCountB: (sumCountA[0] + sumCountB[0], sumCountA[1] + sumCountB[1]))
    seriesToAvgRating = seriesToSumCountRating.mapValues(lambda tup2n:  round(tup2n[0]/tup2n[1], 2))

    # Cartesian para filtrar las series con sus capitulos
    seriesEpisodeRating = tvSeries.map(lambda line: (line[3]+ "#" + line[4], line[7], float(line[2])))
    episodes_together = seriesEpisodeRating.cartesian(seriesMax)

    episodes_together = episodes_together.filter(lambda line: (line[0][0]==line[1][0]) and (line[0][2]==line[1][1]))
    episodes_together = episodes_together.map(lambda line: (line[0][0], line[0][1]))

    # Juntar episodios con mejor rating en una misma string
    episodes_together = episodes_together.map(lambda tup: (tup[0], tup[1]))
    episodes_together = episodes_together.aggregateByKey((''), \
        lambda sumString, tupla: (sumString + '|' + tupla), \
        lambda sumCountA, sumCountB: (sumCountA + sumCountB))

    # Cartesian de average con max
    avg_max_together = seriesToAvgRating.cartesian(seriesMax)
    avg_max_together = avg_max_together.filter(lambda line: (line[0][0]==line[1][0]))
    avg_max_together = avg_max_together.map(lambda line: (line[0][0], (line[0][1], line[1][1])))

    #Join
    episodes_together = episodes_together.join(avg_max_together)
    episodes_together = episodes_together.map(lambda line: (line[0], line[1][0], line[1][1][0], line[1][1][1]))

    episodes_together.saveAsTextFile(fileout);

    spark.stop()