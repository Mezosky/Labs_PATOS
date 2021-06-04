# Laboratorio 8: PageRank over Wikipedia using Giraph

<p align="center">
<img src="https://giraph.apache.org/images/ApacheGiraph.svg" alt="busquedaelasticada" height="300">
</p>


- Conectar a servidor:
        
        ssh -p 220 uhadoop@cm.dcc.uchile.cl

- Password del servidor: 

        HADcc5212$oop

scp -P 220 D:\pandicosas\Codes\Eclipe_projects\Labs_PATOS\Lab8\Code\mdp-giraph\dist\mdp-giraph.jar uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/

giraph mdp-giraph.jar org.mdp.hadoop.cli.PageRank -eif org.mdp.hadoop.io.TextNullTextEdgeInputFormat -eip /uhadoop/shared/wiki/es-wiki-links.tsv.gz -vof org.mdp.hadoop.io.VertexValueTextOutputFormat -op /uhadoop2021/grupo34/pr-full -w 4 -ca mapreduce.job.tracker=yarn -ca mapreduce.framework.name=yarn -mc org.mdp.hadoop.pr.PageRankAgg


hadoop jar mdp-giraph.jar SortByRank -D mapreduce.job.reduces=1 /uhadoop2021/grupo34/pr-full/ /uhadoop2021/grupo34/pr-full-s/

hdfs dfs -copyToLocal /uhadoop2021/grupo34/pr-full-s/part-r-00000 /data/2021/uhadoop/grupo34/ranks.s.tsv

java -jar mdp-lab07.jar BuildWikiIndexBulkWithRank -i /data/uhadoop/shared/wiki/es-wiki-articles.tsv.gz -igz -p /data/2021/uhadoop/grupo34/ranks.s.tsv -o wikig34lab8

- Comando para obtener el top de busquedas

hdfs dfs -cat  /uhadoop2021/grupo34/pr-full-s/part-r-00000 | head -n 10

- Comando para realizar busquedas

java -jar mdp-lab07.jar SearchWikiIndex -i wikig34lab8
java -jar mdp-lab07.jar SearchWikiIndexWithRank -i wikig34lab8