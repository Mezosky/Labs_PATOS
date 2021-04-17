# Laboratorio 3: Hadoop!

![Pig Apache](https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/Hadoop_logo.svg/1200px-Hadoop_logo.svg.png)

## Objetivos

- Se deberá crear una clase propia que nos permita obtener el numero de veces que una tupla de actores aparece en la misma pelicula.

## Entregables

- Entregar las clases creadas para esta sesión
- El recuento de cualquier palabra que empiece por 's' en la Wikipedia en español (fromCountWords).
- Los recuentos de las diez primeras palabras por frecuencia en la Wikipedia en español         (fromSortWordCounts).
- Los recuentos de los diez primeros coprotagonistas por frecuencia en IMDb.
- El recuento de una pareja de coprotagonistas de la que eres muy fan en IMDb

## Codigos utiles

- Conectar a servidor:
        
        ssh -p 220 uhadoop@cm.dcc.uchile.cl

- Password del servidor: HADcc5212$oop

- Enviar archivos a LFS: 

        scp -P 220 local-path D:/pandicosas/Codes/Eclipe_projects/Lab3/mdp-hadoop/dist/mdp-hadoop.jar uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/

- Correr clases:

        Primer job a correr:
        hadoop jar /data/2021/uhadoop/grupo34/mdp-hadoop.jar ActorsTogether2 /uhadoop/shared/imdb/imdb-stars.tsv /uhadoop2021/g34/imbd/
        
        Segundo job a correr:
        hadoop jar /data/2021/uhadoop/grupo34/mdp-hadoop.jar ActorsTogetherCount /uhadoop2021/g34/imbd/ /uhadoop2021/g34/cimbd/

        Tercer job a correr:
        hadoop jar /data/2021/uhadoop/grupo34/mdp-hadoop.jar SortWordCounts /uhadoop2021/g34/cimbd/ /uhadoop2021/g34/simbd/

- llamadas interesantes:

        hdfs dfs -cat /uhadoop2021/g34/simbd/part-r-00000 | head -n 50
        
        hdfs dfs -cat /uhadoop2021/g34/simbd/part-r-00000 | grep -e "^dicaprio" | more  

- ¿Como borro carpetas creadas?
        
        hdfs dfs -rmr /uhadoop2021/g34/imbd/