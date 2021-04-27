# Laboratorio 5: Apache Spark

![apacheSpark](https://www.josebernalte.com/wp-content/uploads/2018/02/spark.png)

## Objetivos

- Se deberá generar un archivo con las siguientes caracteristicas:

> ```The Wire#2002        -30- (#5.10)|Final Grades (#4.13)|  Middle Ground (#3.11)        9.6        8.835```


## Entregables

- Subir la clase o programa InfoSeriesRating (Escrita en Java o Python). 
- La tupla de salida para la seria ”The Simpsons#1989” y otro resultado a nuestra elección.

## Codigos Utiles

- Entrar al servidor SSH:

        ssh -p 220 uhadoop@cm.dcc.uchile.cl

- Contraseña del servidor: 
        
        HADcc5212$oop

- Entrar al HDFS (Donde estan alojados los archivos que se procesan):

        hdfs dfs -ls /uhadoop2021/grupo34

- Enviar archivo al SSH:

        scp -P 220 D:/pandicosas/Codes/Eclipe_projects/Labs_PATOS/Lab5/Code/Java/py/average_series_rating.py uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/

        scp -P 220 D:/pandicosas/Codes/Eclipe_projects/Labs_PATOS/Lab5/Code/Java/py/test.py uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/

        scp -P 220 D:/pandicosas/Codes/Eclipe_projects/Labs_PATOS/Lab5/Code/Java/py/test2.py uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/

- ¿Que tienen los archivos de prueba?:

        cat uhadoop/shared/imdb/imdb-ratings-test.tsv 

- Ejecutar codigo Python:

        spark-submit --master spark://cluster-01:7077 wordcount.py hdfs://cm:9000/uhadoop/shared/wiki/es-wiki-abstracts-100l.txt hdfs://cm:9000/uhadoop2021/grupo34/

        spark-submit --master spark://cluster-01:7077 average_series_rating.py hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings-test.tsv hdfs://cm:9000/uhadoop2021/grupo34/test

        spark-submit --master spark://cluster-01:7077 test.py hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings-test.tsv hdfs://cm:9000/uhadoop2021/grupo34/test

        spark-submit --master spark://cluster-01:7077 test2.py hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings-test.tsv hdfs://cm:9000/uhadoop2021/grupo34/test

- Ver resultados de la salida:

        hdfs dfs -cat /uhadoop2021/grupo34/test/part-00000
        hdfs dfs -cat /uhadoop2021/grupo34/test/part-00001

- ¿ Como borro los archivos ?

        hdfs dfs -rmr /uhadoop2021/grupo34/test/

- Me da flojera escribir a cada rato lo mismo, aquí tienes un script:

                hdfs dfs -rmr /uhadoop2021/grupo34/test/
                cd grupo34
                spark-submit --master spark://cluster-01:7077 test2.py hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings-test.tsv hdfs://cm:9000/uhadoop2021/grupo34/test