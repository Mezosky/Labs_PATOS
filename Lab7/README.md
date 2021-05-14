# Laboratorio 7: Elasticsearch

<p align="center">
<img src="https://blog.bismart.com/hs-fs/hubfs/Imported_Blog_Media/Elastic%20Search/Elasticsearch%20GIF.gif?width=900&name=Elasticsearch%20GIF.gif" alt="busquedaelasticada" height="300">
</p>

## Objetivos

- Construir las clases ```BuildWikiIndexBulk``` y ```SearchWikiIndex``` para realizar búsquedas (ultra rápidas) en muchos datos 😱.

<p align="center">
<img src="https://lh3.googleusercontent.com/proxy/FdTczxwSWx4NH6QOwK1CKry0TXrAPUUC16Xy4oC9df97_1xsLVCPsdi71sW8_oJI10t52JOR3xefDqWIlDrBsmhuBqQhKAGaDWFp4EJCnMK5xLJVxKByOx_ix3fFTdXujEw0RXXcd9Wuom2mVqx4PcfEjwPKHMfvce4" alt="busquedaelasticada" height="300">
</p>

## Entregables

- Subir los códigos ```BuildWikiIndexBulk``` y ```SearchWikiIndex```.

- Realizar la búsqueda de "obama" y 4 términos de interés.... 🤔.

## Codigos

Hermanito este proyecto tiene muchos archivos, ¿dónde está el Código principal?. A continuación, puedes encontrar las clases:

> [`BuildWikiIndexBulk.java`](https://github.com/Mezosky/Labs_PATOS/blob/main/Lab7/Codes/mdp-elasticsearch/src/cl/uchile/pmd/BuildWikiIndexBulk.java)
> [`SearchWikiIndex.java`](https://github.com/Mezosky/Labs_PATOS/blob/main/Lab7/Codes/mdp-elasticsearch/src/cl/uchile/pmd/SearchWikiIndex.java)

<p align="center">
<img src="https://media.tenor.com/images/a2f66c292f5a4fa9fd898bd06ddcfcbe/tenor.gif" alt="busquedaelasticada" height="300">
</p>

## Resultados

- Se realizo la Busqueda de las siguientes palabras:

    - "obama"
    - "huxley"
    - "ng"
    - "k-pop"
    - "feynman"

Donde, el archivo con los resultados es:
> [`results.txt`](https://github.com/Mezosky/Labs_PATOS/blob/main/Lab7/Resultados/results.txt)

## Codigos utiles para la ejecución del Lab

- Crear index

        curl -X PUT "cm:9200/wikig34?pretty" -H 'Content-Type: application/json' -d'{
            "mappings" : {
                "_doc" : {
                    "properties" : {
                        "TITLE" : {
                            "type" : "text",
                            "store" : "true",
                            "analyzer" : "spanish"
                        }, 
                        "ABSTACT" : {
                            "type" : "text",
                            "store" : "true",
                            "analyzer" : "spanish"
                        }, 
                        "URL" : {"type" : "text",
                                "store" : "true",
                                "analyzer" : "spanish"}, 
                        "MODIFIED" : {"type" : "date"
                        }
                    }
                }
            }
        }'

- Conectar a servidor:
        
        ssh -p 220 uhadoop@cm.dcc.uchile.cl

- Password del servidor: 

        HADcc5212$oop

- Enviar archivos al LFS

        scp -P 220 D:\pandicosas\Codes\Eclipe_projects\Labs_PATOS\Lab7\Codes\mdp-elasticsearch\dist\mdp-elasticsearch.jar uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/

- Obtener archivos del LFS: 

        scp -P 220 uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/results_lab7.txt C:/Users/imeza/Desktop/

- Ejecutar el constructor de indexado

        java -jar mdp-elasticsearch.jar BuildWikiIndexBulk -i /data/uhadoop/shared/wiki/es-wiki-articles.tsv.gz -igz -o wikig34


- Ejecutar el buscador sonico:

        java -jar mdp-elasticsearch.jar SearchWikiIndex -i wikig34 > results_lab7.txt