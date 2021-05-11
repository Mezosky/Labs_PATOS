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
if (Majortwits.size() == FIFO_SIZE) {
        // Obtenemos el primer y ultimo elemento de la lista
	String strFirstTwit = Majortwits.getFirst();
	String strLastTwit = Majortwits.getLast();
        // Separamos los elementos
	String[] tabsFirstTwit = strFirstTwit.split("\t");
	String[] tabsLastTwit = strLastTwit.split("\t");
	// Obtenemos el tiempo en el formato Timestamp
	long timeDataFirstTwit = TWITTER_DATE.parse(tabsFirstTwit[0]).getTime();
	long timeDataLastTwit = TWITTER_DATE.parse(tabsLastTwit[0]).getTime();
	// Realizamos la resta, y transformamos a segundos
	long timeDelta = (timeDataLastTwit - timeDataFirstTwit)/1000;
        // Realizamos condicionales para la busqueda de las rafagas major/minor
	if (timeDelta <= 25 && !boolMajor && timeDelta > 0) {
                boolMajor = true;
		BurstMajor.addLast(strFirstTwit);
		Majortwits.removeFirst();
	} else if (timeDelta > 25 && boolMajor){
		boolMajor = false;
		Majortwits.clear();
		Majortwits.addLast(lowercase);
	} else {
		Majortwits.removeFirst();
                }
	}
if (Minortwits.size() == FIFO_SIZE) {
	// Obtenemos el primer y ultimo elemento de la lista
	String strFirstTwit = Minortwits.getFirst();
	String strLastTwit = Minortwits.getLast();
	// Separamos los elementos
	String[] tabsFirstTwit = strFirstTwit.split("\t");
	String[] tabsLastTwit = strLastTwit.split("\t");
	// Obtenemos el tiempo en el formato Timestamp
	long timeDataFirstTwit = TWITTER_DATE.parse(tabsFirstTwit[0]).getTime();
	long timeDataLastTwit = TWITTER_DATE.parse(tabsLastTwit[0]).getTime();
	// Realizamos la resta, y transformamos a segundos
	long timeDelta = (timeDataLastTwit - timeDataFirstTwit)/1000;
	// Realizamos condicionales para la busqueda de las rafagas major/minor
	if (timeDelta <= 50 && !boolMinor && timeDelta > 0) {
		boolMinor = true;
		BurstMinor.addLast(strFirstTwit);
		Minortwits.removeFirst();
		} else if (timeDelta > 50 && boolMinor){
			boolMajor = false;
			Minortwits.clear();
			Minortwits.addLast(lowercase);
		} else {
			Minortwits.removeFirst();
			}
    }
```
## Clase creada

Hermanito, antes de pegarte el show, ¿Dónde están los códigos?...


> [Producer: `EarthquakeFilter.java`](https://github.com/Mezosky/Labs_PATOS/blob/main/Lab6/Code/mdp-kafka/src/org/mdp/kafka/cli/MyProducer.java)

> [Consumer: `BurstDetector.java`](https://github.com/Mezosky/Labs_PATOS/blob/main/Lab6/Code/mdp-kafka/src/org/mdp/kafka/cli/MyConsumer.java)

## Resultados

<p align="center">
  <img src="https://engineering.giphy.com/wp-content/uploads/2018/01/fiking.gif">
</p>

Resulto ser mas información de lo que se creia......

- Resumen de los resultados encontrados:

        ##########Resumen########## 
        Fueron encontrados 333 Major burst. 
        Fueron encontrados 1 Minor burst. 

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

- Obtener archivos del LFS: 

        scp -P 220 uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/results.txt C:/Users/imeza/Desktop/

- Correr Ejemplos:

        kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic grupo34-example
        kafka-topics.sh --list --zookeeper localhost:2181

        java -jar mdp-kafka.jar KafkaExample grupo34-example
        kafka-topics.sh --delete --zookeeper localhost:2181 --topic grupo34-example


        kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic grupo34-tweets
        java -jar mdp-kafka.jar TwitterSimulator /data/uhadoop/shared/twitter/tweets_20170919.tsv.gz grupo34-tweets 100

- Correr codigos del Lab:

        java -jar mdp-kafka.jar MyProducer /data/uhadoop/shared/twitter/tweets_20170919.tsv.gz grupo34-tuits
        java -jar mdp-kafka.jar MyConsumer2 grupo34-tuits > results.txt



        