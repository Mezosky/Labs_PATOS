ssh -p 220 uhadoop@cm.dcc.uchile.cl
PW: HADcc5212$oop

curl -X GET "cm:9200/_cat/health?v&pretty"

curl -X PUT "cm:9200/customer/_doc/1?pretty" -H 'Content-Type: application/json' -d '{ "name": "Fin Shepard" }'

curl -X GET "cm:9200/customer/_doc/1?pretty"

curl "cm:9200/_cat/indices?v"

curl -X DELETE "cm:9200/customer?pretty"

curl -X PUT "cm:9200/mywiki?pretty" -H 'Content-Type: application/json' -d'{"mappings" : {"_doc" : {"properties" : {"TITLE" : {"type" : "text","store" : "true","analyzer" : "standard"}}}}}'

- Enviar archivos al LFS

        scp -P 220 D:\pandicosas\Codes\Eclipe_projects\Labs_PATOS\Lab7\Codes\mdp-elasticsearch\dist\mdp-elasticsearch.jar uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/