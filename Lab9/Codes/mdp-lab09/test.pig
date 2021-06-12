register /data/hadoop/.m2/repository/com/datastax/cassandra/cassandra-driver-core/2.0.1/*.jar;
register /data/hadoop/cassandra/lib/*.jar;
define CqlStorage org.apache.cassandra.hadoop.pig.CqlStorage();

testin = LOAD 'cql://cc5212/tabla34csv/' USING CqlStorage;

-- Agrupamos y sumamos
grouped_data = GROUP testin BY group;
output_sum = FOREACH grouped_data GENERATE COUNT(testin) AS counter, group AS grupos;

-- Agrupamos los Keys en el formato de cassandra
cassandra_with_key = FOREACH output_sum GENERATE TOTUPLE(TOTUPLE('group',grupos)), TOTUPLE(counter);

-- Almacenar los datos
STORE cassandra_with_key INTO 'cql://cc5212/testout34?output_query=UPDATE+cc5212.testout34+SET+groupsize+%3D+%3F' USING CqlStorage;