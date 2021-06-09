-- log into CQLSH first "cqlsh 10.10.10.1" 
-- note you should perhaps use unique table names here
-- use cc5212;
-- CREATE TABLE testin ( name text PRIMARY KEY, val int, word text );
-- INSERT INTO testin( name, val, word ) VALUES ( 'a', 1, 'apple' );
-- INSERT INTO testin( name, val, word ) VALUES ( 'b', 2, 'banana' );
-- INSERT INTO testin( name, val, word ) VALUES ( 'c', 3, 'crispy' );
-- CREATE TABLE testout ( name text PRIMARY KEY, val int, word text );


-- then you can run the following PIG code with "pig test.pig"
-- or just type "pig" and enter the commands interactively


register /data/hadoop/.m2/repository/com/datastax/cassandra/cassandra-driver-core/2.0.1/*.jar;
register /data/hadoop/cassandra/lib/*.jar;
define CqlStorage org.apache.cassandra.hadoop.pig.CqlStorage();

testin = LOAD 'cql://cc5212/testin/' USING CqlStorage;

DESCRIBE testin;
-- testin: {name: chararray,val: int,word: chararray}

smallval = FILTER testin BY val < 3;

-- the first tuple is the primary key
-- you need to give the name of the column key ('username') and then the value
cassandra_with_key = FOREACH smallval GENERATE TOTUPLE(TOTUPLE('name',name)),TOTUPLE(val,word);

-- need to use a "prepared statement" to update the table
-- this needs to be coded as a URL http://www.url-encode-decode.com/
-- the raw query here is: "UPDATE cc5212.testout SET val = ?, word = ?"
-- the parameters follows the same order as the second tuple in cassandra_with_key
-- "WHERE name = ?" is added automatically.

STORE cassandra_with_key INTO 'cql://cc5212/testout?output_query=UPDATE+cc5212.testout+SET+val+%3D+%3F%2C+word+%3D+%3F' USING CqlStorage;

-- Check output in CQLSH:
-- SELECT * FROM testout;