/*
The position column of the credits table indicates an actor's rank on the billing of a movie. So for example, position = 1 represents a billing of a top star. A movie may have several top stars sharing the top billing. Your task is to complete the following query template so that it returns rows of the form name1, name2, title where the names are of two (different) actors sharing the top billing in the movie having the associated title. The first name should be lexicographically less than the second.

   SELECT P1.name AS name1, P2.name AS name2, title
      YOUR-CODE-GOES-HERE
       ORDER BY name1, name2, title ;

*/

SELECT p1.name AS name1, p2.name AS name2, m.title
   FROM people AS p1 
   INNER JOIN credits AS c1 ON c1.person_id = p1.id 
                           AND c1.position = 1
   INNER JOIN credits AS c2 ON c1.movie_id = c2.movie_id
                           AND c2.position = 1 
                           AND c2.person_id <> c1.person_id
   INNER JOIN people AS p2 ON p2.id = c2.person_id
   INNER JOIN movies AS m ON m.id = c2.movie_id
      WHERE p1.name > p2.name
   ORDER BY name1, name2, m.title


