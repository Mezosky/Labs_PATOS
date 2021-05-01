# Laboratorio 5: Apache Kafka

<p align="center">
<img src="https://blogzeent.files.wordpress.com/2016/11/logo.png?w=975" alt="apacheKafka" height="250">
</p>

## Objetivos

- Detectar terremotos con Twitter del año 2017 y Kafka.

## Entregables

- Subir las clases EarthquakeFilter.java y BurstDetector.java creadas en Java.
- Subir el número de sucesos menores/mayores detectados, junto con el registro más reciente detectado para cada uno de ellos (el primero que cumpla los criterios de ráfaga).


## Codigos utiles

- Conectar a servidor:
        
        ssh -p 220 uhadoop@cm.dcc.uchile.cl

- Password del servidor: 

        HADcc5212$oop

- Enviar archivos a LFS: 

        scp -P 220 D:\pandicosas\Codes\Eclipe_projects\Labs_PATOS\Lab6\Code\mdp-kafka\dist\mdp-kafka.jar uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/

- Correr Ejemplos:

        kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic grupo34-example
        kafka-topics.sh --list --zookeeper localhost:2181

        java -jar mdp-kafka.jar KafkaExample grupo34-example
        kafka-topics.sh --delete --zookeeper localhost:2181 --topic grupo34-example

        kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic grupo34-tweets
        java -jar mdp-kafka.jar TwitterSimulator /data/uhadoop/shared/twitter/tweets_20170919.tsv.gz grupo34-tweets 100

- Correr codigos del Lab:

        java -jar mdp-kafka.jar PrintEarthquakeTweets grupo34-tweets