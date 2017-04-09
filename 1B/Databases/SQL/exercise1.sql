/*
    EXERCISE : 

    Complete the following query template to produce a query that returns rows name, total where the name column is the name of a person, and the total column indicates the number of movies of the genre Drama for which that person is a writer.

       SELECT name, count(*) AS total
          YOUR-CODE-GOES-HERE
        GROUP BY name
        ORDER BY total desc, name
        LIMIT 10;
*/

SELECT p.name, count(*) AS total
      FROM people AS p 
      INNER JOIN credits AS c ON p.id = c.person_id 
      INNER JOIN genres AS g ON c.movie_id = g.movie_ID
          WHERE g.genre = 'Drama' AND c.type = 'writer'
  GROUP BY p.name
  ORDER BY total desc, p.name
  LIMIT 10;
                     
