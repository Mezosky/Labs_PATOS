# Laboratorio 7: Elasticsearch

<p align="center">
<img src="https://blog.bismart.com/hs-fs/hubfs/Imported_Blog_Media/Elastic%20Search/Elasticsearch%20GIF.gif?width=900&name=Elasticsearch%20GIF.gif" alt="busquedaelasticada" height="300">
</p>

## Indice

- [Objetivos](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#Objetivos)
- [Entregables](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#entregables)
- [Codigos](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#Codigos)
- [Creación de Index](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#Creación-de-Index)
- [Resultados](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#Resultados)
- [Codigos utiles para la ejecución del Lab](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#Codigos-utiles-para-la-ejecución-del-Lab)

## Objetivos
[Volver al indice](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#Indice)


- Construir las clases
 ```BuildWikiIndexBulk``` y ```SearchWikiIndex``` para realizar búsquedas (ultra rápidas) en muchos datos 😱. La función principal de estos códigos es la creación de los índices y la búsqueda de palabras claves en base a los índices creados.

<p align="center">
<img src="https://lh3.googleusercontent.com/0qW_zsZnQu_Dpzwc8aoodnzg1c7SjZ8y4TjEWXiTRXG-Bx3z0dxRgjGiU3IGWuJJ8EFsfou0xHVM3Rd6m4MOoJKNpp0quKK3tNKtL80qqpKPI0UcZsLxOn9Pl5FCVw8ewZGusg9N" alt="busquedaelasticada" height="300">
</p>

## Entregables
[Volver al indice](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#Indice)

- Subir los códigos ```BuildWikiIndexBulk``` y ```SearchWikiIndex```.

- Realizar la búsqueda de "obama" y 4 términos de interés.... 🤔.

## Codigos
[Volver al indice](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#Indice)

<p align="center">
<img src="https://media.tenor.com/images/a2f66c292f5a4fa9fd898bd06ddcfcbe/tenor.gif" alt="busquedaelasticada" height="300">
</p>

Hermanito este proyecto tiene muchos archivos, ¿dónde está el Código principal?. A continuación, puedes encontrar las clases:

> [`BuildWikiIndexBulk.java`](https://github.com/Mezosky/Labs_PATOS/blob/main/Lab7/Codes/mdp-elasticsearch/src/cl/uchile/pmd/BuildWikiIndexBulk.java)

> [`SearchWikiIndex.java`](https://github.com/Mezosky/Labs_PATOS/blob/main/Lab7/Codes/mdp-elasticsearch/src/cl/uchile/pmd/SearchWikiIndex.java)

## Creación de Index:
[Volver al indice](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#Indice)

La parte primordial para realizar este laboratorio se centra en la creación de los índices adecuados para realizar las búsquedas. Para definimos el índex ```wikig34``` de la siguiente manera:

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

## Resultados
[Volver al indice](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#Indice)

<p align="center">
<img src="https://thumbs.gfycat.com/ShamefulDisfiguredHorseshoebat-max-1mb.gif" alt="kpopit" height="300">
</p>

Una vez elaborado el Código, realizamos las siguientes búsquedas:

    "obama"
    "huxley"
    "ng"
    "k-pop"
    "feynman"

Donde, se obtienen los siguientes resultados:

> [`results.txt`](https://raw.githubusercontent.com/Mezosky/Labs_PATOS/main/Lab7/Resultados/results.txt)

## Codigos utiles para la ejecución del Lab
[Volver al indice](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab7#Indice)

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

---

<p align="center">
<img src="https://kanikag.com/images/articles-Women-Comics-Pig-Hooey-giphy.gif" height="300">
</p>