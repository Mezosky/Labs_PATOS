# Laboratorio 4: Apache Pig

![Pig Apache](https://miro.medium.com/max/1838/1*v1dRCjcQMoXDOpsWD79CQA.png)

## Objetivos

- El objetivo principal del laboratorio es identificar los mejores actores/actrices en el dataset de IMDb, los cuales serán definidos como los actores que aparecen en las mejores películas.

- Para esto se debe codificar el script `top-stars.pig`, quien deberá ejecutar el objetivo señalado.

## Codigos utiles
- ssh -p 220 uhadoop@cm.dcc.uchile.cl
- Password del servidor: HADcc5212$oop
- Enviar archivos a LFS: scp -P 220 local-path {archivo a enviar} uhadoop@cm.dcc.uchile.cl:/data/2021/uhadoop/grupo34/
- Run pig apache: pig costar-count.pig o pig -x local