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

## To-Do

- [x] Filtrar por THEATRICAL_MOVIE.
- [x] Filtrar por mejores peliculas.
- [x] Split actores en actores masculinos y femeninos.
- [x] Contar actores/actrices que aparecieron en las mejores peliculas.
- [x] Ordenar de mayor a menor el conteo de los actores/actrices.
- [X] Realizar el Store de los jobs (separado por genero).
- [ ] Revisar tareas anteriores.

## Entregables

- Subir el script `top-stars.pig` con el top de 10 actores, top 10 actrices y 3 random resultados de actores o actrices que nos gusten.