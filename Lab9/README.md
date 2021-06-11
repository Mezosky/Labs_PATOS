- Conectar a servidor:
        
        ssh -p 220 uhadoop@cm.dcc.uchile.cl

- Password del servidor: 

        HADcc5212$oop

cqlsh 10.10.10.1

CREATE keyspace grupo34 with replication ={ 'class' : 'SimpleStrategy', 'replication_factor' : 3 };

DESCRIBE keyspaces;

USE grupo34;

CREATE TABLE tabla34 ( username text, age int);

CREATE TABLE tabla34 ( username text PRIMARY KEY, age int);

DESCRIBE tables;

ALTER TABLE tabla34 ADD awake boolean;

DROP TABLE tabla34;
DROP KEYSPACE grupo34;

USE cc5212;
DESCRIBE tables;
DESCRIBE TABLE students;
SELECT * FROM students;

INSERT INTO students( username, name ) VALUES ( 'Espina', 'Jose' );

INSERT INTO students( username, name ) VALUES ( 'Espina', true );

DELETE name FROM students WHERE username = 'Espina';

DELETE FROM students WHERE username = 'Espina';

INSERT INTO students( username, name ) VALUES ('Espina', 'José Guillermo');

INSERT INTO students(username,  awake, group, hobbies, message) VALUES ('Espina',True, 'grupo34', {'Musica', 'mimir'}, 'Viva el mate!');

SELECT COUNT(*) AS scount FROM students;

SELECT name FROM students WHERE username = 'Espina';

SELECT name FROM students WHERE age = 22;

COPY students (username, name, age, group, hobbies, awake, message) TO 'grupo34.csv';

CREATE TABLE tabla34csv ( username text PRIMARY KEY, , name text, age int, group text, hobbies set<text>, awake boolean, message text);

COPY tabla34csv (username, name, age, group, hobbies, awake, message) FROM 'grupo34.csv';
