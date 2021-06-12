# Lab 9: Cassandra

<p align="center">
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Cassandra_logo.svg/1200px-Cassandra_logo.svg.png" alt="busquedaelasticada" height="300">
</p>

## Indice

- [Objetivos](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab9#Objetivos)
- [Entregables](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab9#entregables)
- [Códigos](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab9#Codigos)
- [Resultados](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab9#Resultados)
- [Códigos utiles para la ejecución del Lab](https://github.com/Mezosky/Labs_PATOS/tree/main/Lab9#Códigos-útiles-para-la-ejecución-del-cluster-y-otros)


## Objetivos

- [X] Realizar múltiples consultas en Cassandra.
- [X] Contar el número de alumnos que existen por grupos en la tabla de estudiantes. Para esto se deberá utilizar las tecnologías Apache Pig y Cassandra.

## Entregables

- Se deberan subir todas las consultas realizadas en Cassandra y el script de pig modificado.

## Códigos

A continuación, se adjunta el codigo de pig apache:

[`pig_cassandra.pig`](https://github.com/Mezosky/Labs_PATOS/blob/main/Lab9/Codes/mdp-lab09/test.pig)

Por otro lado en el siguiente archivo de texto son adjuntadas las consultas realizadas:

[`Lab9.txt`](https://github.com/Mezosky/Labs_PATOS/blob/main/Lab9/Codes/lab9.txt)

## Resultados

A continuación, son expuestos los resultados obtenidos utilizando las tecnologías Apache Pig y Cassandra:

<center>

 group      | groupsize  
|------------|-----------|
|         46 |         3 |
|   group 5  |         1 |
|    group15 |         3
|        G46 |         1
|         43 |         1
|         39 |         1
|        G48 |         1
|         19 |         1
|         48 |         1
|      profe |         3
| lasdivinas |         1
|         g8 |         1
|          7 |         1
|          9 |         1
|        g44 |         1
|         45 |         2
|          3 |         1
|    group11 |         3
|         50 |         1
|        G40 |         1
|        G17 |         1
|    Grupo 9 |         1
|    grupo34 |         1
|         12 |         1
|         G2 |         1
|  grupito19 |         1
|    grupo 4 |         1
|    grupo25 |         2
|        G55 |         1
|         41 |         1

</center>

## Códigos utiles para la ejecución del cluster y otros

- Conectar a servidor:
        
        ssh -p 220 uhadoop@cm.dcc.uchile.cl

- Password del servidor: 

        HADcc5212$oop

- Inicializar Cassandra:
        
        cqlsh 10.10.10.1
