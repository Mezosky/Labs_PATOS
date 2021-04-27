# Laboratorio 5: Apache Spark

<p align="center">
<img src="https://www.josebernalte.com/wp-content/uploads/2018/02/spark.png" alt="apacheSpark" height="250">
</p>

## Indice

- [Objetivos](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab5#Objetivos)
- [Entregables](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab5#entregables)
- [Codigos Utiles](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab5#Codigos-Utiles)
- [Resultados](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab5#Resultados)
- [Otros](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab5#Otros)

## Objetivos

- Se deberá generar un archivo con las siguientes caracteristicas:

> ```The Wire#2002        -30- (#5.10)|Final Grades (#4.13)|  Middle Ground (#3.11)        9.6        8.835```

<p align="center">
  <img src="https://thumbs.gfycat.com/ThinObviousAmericanbittern-max-1mb.gif">
</p>

## Entregables

- Subir la clase o programa InfoSeriesRating (Escrita en Java o Python). 
- La tupla de salida para la seria ”The Simpsons#1989” y otro resultado a nuestra elección.

## Codigos Utiles

- Entrar al servidor SSH:

        ssh -p 220 uhadoop@cm.dcc.uchile.cl

- Contraseña del servidor: 
        
        HADcc5212$oop

- "Mirar" el HDFS (Donde estan alojados los archivos que se procesan):

        hdfs dfs -ls /uhadoop2021/grupo34

- Enviar archivo al SSH:

        scp -P 220 D:/pandicosas/Codes/Eclipe_projects/Labs_PATOS/Lab5/Code/Java/py/average_series_rating.py uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/

        scp -P 220 D:/pandicosas/Codes/Eclipe_projects/Labs_PATOS/Lab5/Code/Java/py/test.py uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/

        scp -P 220 D:/pandicosas/Codes/Eclipe_projects/Labs_PATOS/Lab5/Code/Java/py/test2.py uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/

        scp -P 220 D:/pandicosas/Codes/Eclipe_projects/Labs_PATOS/Lab5/Code/Java/py/intentoJose2.py uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/

- ¿Que tienen los archivos de prueba?:

        cat uhadoop/shared/imdb/imdb-ratings-test.tsv 

- Ejecutar codigo Python:

        spark-submit --master spark://cluster-01:7077 wordcount.py hdfs://cm:9000/uhadoop/shared/wiki/es-wiki-abstracts-100l.txt hdfs://cm:9000/uhadoop2021/grupo34/

        spark-submit --master spark://cluster-01:7077 average_series_rating.py hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings-test.tsv hdfs://cm:9000/uhadoop2021/grupo34/test

        spark-submit --master spark://cluster-01:7077 test.py hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings-test.tsv hdfs://cm:9000/uhadoop2021/grupo34/test

        spark-submit --master spark://cluster-01:7077 test2.py hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings-test.tsv hdfs://cm:9000/uhadoop2021/grupo34/test

        spark-submit --master spark://cluster-01:7077 intentoJose2.py hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings-test.tsv hdfs://cm:9000/uhadoop2021/grupo34/test

- Ver resultados de la salida:

        hdfs dfs -cat /uhadoop2021/grupo34/test/part-00000
        hdfs dfs -cat /uhadoop2021/grupo34/test/part-00001

- ¿ Como borro los archivos ?

        hdfs dfs -rmr /uhadoop2021/grupo34/test/

- Me da flojera escribir a cada rato lo mismo, aquí tienes un script:

                hdfs dfs -rmr /uhadoop2021/grupo34/test/
                cd grupo34
                spark-submit --master spark://cluster-01:7077 intentoJose2.py hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings.tsv hdfs://cm:9000/uhadoop2021/grupo34/test

- Buscar serie de interes:

                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00016 | grep -e "^(u'The Simpsons#1989" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00035 | grep -e "^(u'Twin Peaks" | more


## Resultados

- Busqueda de ```The Simpsons#1989``` entrega:

<p align="center">
  <img src="https://deadhomersociety.files.wordpress.com/2018/01/whochewed.gif">
</p>

> (u'The Simpsons#1989', u"Homer's Enemy (#8.23)", 9.2, 7.38)

- Busqueda de ```Twin Peaks#1990``` entrega:

<p align="center">
  <img src="https://media1.giphy.com/media/3ohzdKjSSVObhesnFm/giphy.gif">
</p>

> (u'Twin Peaks#1990', u'(#2.7)', 9.4, 8.52)

## Otros

- Script para buscar en todos los archivos:

                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00000 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00001 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00002 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00003 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00004 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00005 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00006 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00007 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00008 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00009 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00010 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00011 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00012 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00013 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00014 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00015 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00016 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00017 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00018 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00019 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00020 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00021 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00022 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00023 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00024 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00025 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00026 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00027 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00028 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00029 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00030 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00031 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00032 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00033 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00034 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00035 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00036 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00037 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00038 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00039 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00040 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00041 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00042 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00043 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00044 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00045 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00046 | grep -e "^(u'Twin Peaks" | more
                hdfs dfs -cat /uhadoop2021/grupo34/test/part-00047 | grep -e "^(u'Twin Peaks" | more