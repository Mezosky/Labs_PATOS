# Laboratorio 6: Apache Kafka

<p align="center">
<img src="https://rajvansia.com/images/KafkaVitals.gif" alt="apacheKafka" height="300">
</p>

## Objetivos

- "Detectar" terremotos a traves de Twitters del año 2017 y Kafka (en verdad detectaremos rafagas de twits 😔).

<p align="center">
  <img height=200 src="https://media1.tenor.com/images/a40f47a6377a1b6e633c3a4c5965bf5e/tenor.gif?itemid=7423490">
</p>

## Entregables

- Subir las clases EarthquakeFilter.java y BurstDetector.java creadas en Java.
- Subir el número de sucesos menores/mayores detectados, junto con el registro más reciente detectado para cada uno de ellos (el primero que cumpla los criterios de ráfaga).

<p align="center">
  <img height=200 src="https://images.squarespace-cdn.com/content/v1/5bff4a2a75f9eec627d36396/1594578696859-X60SYJGSG86XHAVBT4I9/ke17ZwdGBToddI8pDm48kNKU_v8gJAcxDrmB-soKvj1Zw-zPPgdn4jUwVcJE1ZvWEtT5uBSRWt4vQZAgTJucoTqqXjS3CfNDSuuf31e0tVHCnm8a75afeGmEHZWkl5PmyTMMaUTKBnPyCeVGtabJSWQ6l2WM7tn7mqHTODzkmeM/tenor-2.gif">
</p>


## Metodología

La "magia" de la detección se encontrará en el Consumer, quien a través de condicionales detectará las ráfagas de tweets vayan apareciendo. Para detectar las ráfagas se ha utilizado una lista enlazada, la cual se va rellenando con 50 elementos en cada iteración. Con esto, se restan los Timestamp del primer y último elemento de la lista para obtener un timeDelta, si este resultado es menor o igual a los segundos de las ráfagas mayor/menor, se registra la existencia de una ráfaga a través de un booleano (más detalles en el Código de más abajo).

>Notar: Se utiliza un timeDelta>0 para evitar potenciales errores en el orden de los tweets (encontramos algunos).

```Java
if (timeDelta <= 25 && !burst25 && timeDelta > 0) {
							burst25 = true;
							Minortwits.addLast(strFirstTwit);
						} else if (timeDelta > 25 && burst25  && timeDelta > 0){
							burst25 = false;
						}
						if (timeDelta <= 50 && !burst50  && timeDelta > 0) {
							burst50 = true;
							Majortwits.addLast(strFirstTwit);
						} else if (timeDelta > 50 && burst50  && timeDelta > 0){
							burst50 = false;
						}
						twits.clear();
```

## Resultados

<p align="center">
  <img src="https://engineering.giphy.com/wp-content/uploads/2018/01/fiking.gif">
</p>

Resulto ser mas información de lo que se creia......

- Resumen de los resultados encontrados:

        Fueron encontrados 412 Minor burst.
        Fueron encontrados 343 Major burst.

- Donde la salida generada (solicitada) es de la forma:

> [`results.txt`](https://raw.githubusercontent.com/Mezosky/Labs_PATOS/main/Lab6/Results/results.txt)

## Codigos utiles

<p align="center">
  <img src="https://www.therpf.com/forums/attachments/717068">
</p>

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

        java -jar mdp-kafka.jar TwitterSimulator /data/uhadoop/shared/twitter/tweets_20170919.tsv.gz grupo34-tweets 1000
        java -jar mdp-kafka.jar MyProducer grupo34-tweets grupo34-tuits
        java -jar mdp-kafka.jar MyConsumer3 grupo34-tuits


        