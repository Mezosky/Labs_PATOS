# Laboratorio 4: Apache Pig

## Objetivos

- The main goal of the lab is to identify the best actors/actresses in IMDb, which we will define as those who
acted in the most good movies.

## Codigos utiles
- ssh -p 220 uhadoop@cm.dcc.uchile.cl
Password: HADcc5212$oop

- Enviar archivos a LFS: scp -P 220 local-path D:/pandicosas/Codes/Eclipe_projects/Lab3/mdp-hadoop/dist/mdp-hadoop.jar uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/
- Run pig apache: pig costar-count.pig o pig -x local