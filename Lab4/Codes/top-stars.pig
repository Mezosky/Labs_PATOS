-- This script finds the actors/actresses with the highest number of good movies

raw_roles = LOAD 'hdfs://cm:9000/uhadoop/shared/imdb/imdb-stars.tsv' USING PigStorage('\t') AS (star, title, year, num, type, episode, billing, char, gender);
-- Later you can change the above file to 'hdfs://cm:9000/uhadoop/shared/imdb/imdb-stars.tsv' to see the full output


raw_ratings = LOAD 'hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings.tsv' USING PigStorage('\t') AS (dist, votes, score, title, year, num, type, episode);
-- Later you can change the above file to 'hdfs://cm:9000/uhadoop/shared/imdb/imdb-ratings.tsv' to see the full output

--------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------
-- Now to implement the script

-- We want to compute the top actors / top actresses (separately).
-- Actors should be one output file, actresses in the other.
-- Gender is now given as 'MALE'/'FEMALE' in the gender column of raw_roles

-- To do so, we want to count how many good movies each starred in.
-- We count a movie as good if:
--   it has at least (>=) 10,001 votes (votes in raw_rating) 
--   it has a score >= 7.8 (score in raw_rating)

-- The best actors/actresses are those with the most good movies.

-- An actor/actress plays one role in each movie 
--   (more accurately, the roles are concatenated on one line like "role A/role B")

-- If an actor/actress does not star in a good movie
--  a count of zero should be returned (i.e., the actor/actress
--   should still appear in the output).

-- The results should be sorted descending by count.

-- We only want to count entries of type THEATRICAL_MOVIE (not tv series, etc.).
-- Again, note that only CONCAT(title,'##',year,'##',num) acts as a key for movies.

-- Test on smaller file first (as given above),
--  then test on larger file to get the results.

--------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------

-- Filtramos por Theatrical Movie cada uno de los dataset.
-- (Duda: Es mas eficiente filtrar o trabajar con los datos de actores sin filtrar?)
raw_roles_f = FILTER raw_roles BY type == 'THEATRICAL_MOVIE';
good_movies = FILTER raw_ratings BY ((votes >= 10001) AND (score >= 7.8) AND (type == 'THEATRICAL_MOVIE'));

-- Split para generar actors y actresses por separado.
SPLIT raw_roles_f INTO actors IF gender=='MALE', actresses IF gender=='FEMALE';
	
-- Generar la key para realizar la concatenación entre los actores y el dataset de peliculas.
actors_join = FOREACH actors GENERATE CONCAT(title,'##',year,'##',num) AS key, star;
actresses_join = FOREACH actresses GENERATE CONCAT(title,'##',year,'##',num) AS key, star;
good_movies_join = FOREACH good_movies GENERATE CONCAT(title,'##',year,'##',num) AS key;

-- Concatenar actors y actressses con las peliculas.
best_actors = JOIN actors_join BY key LEFT OUTER, good_movies_join BY key;
best_actresses = JOIN actresses_join BY key LEFT OUTER, good_movies_join BY key;

-- Al realizar el JOIN LEFT OUTER se generaran valores nules, que representaran peliculas 
-- "no tan buenas" en las que participaron los actores. Por esto, es anexado un 0 si son
-- nulos o un 1 en cualquier otro caso.

best_actors_count = FOREACH best_actors GENERATE star, (
	CASE
		WHEN $2 is null THEN 0
		ELSE 1
	END
) AS count;

best_actresses_count = FOREACH best_actresses GENERATE star, (
	CASE
		WHEN $2 is null THEN 0
		ELSE 1
	END
) AS count;

-- Sumamos todos los valores para los actores/actrices.
best_actors_group = GROUP best_actors_count by star;
best_actors_total = FOREACH best_actors_group GENERATE $0 AS star, SUM(best_actors_count.count) as count;

best_actresses_group = GROUP best_actresses_count by star;
best_actresses_total = FOREACH best_actresses_group GENERATE $0 AS star, SUM(best_actresses_count.count) as count;

-- Reordenamos de mayor a menor los actores/actrices.
best_actors_sorted = ORDER best_actors_total BY count DESC, star ASC;
best_actresses_sorted = ORDER best_actresses_total BY count DESC, star ASC;

-- Generamos los archivos de salida
STORE best_actors_sorted INTO '/uhadoop2021/grupo34/best_actors/';
STORE best_actresses_sorted INTO '/uhadoop2021/grupo34/best_actresses/';

