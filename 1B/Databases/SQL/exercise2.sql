/*
    EXERCISE TEXT: 
    
    Now modify your solution for Exercise 1 so that names and totals are associated with those writers that write ONLY for movies associated ONLY with the genre Drama.

*/

SELECT p.name, count(*) AS total
   FROM people AS p 
   INNER JOIN credits AS c ON p.id = c.person_id 
   INNER JOIN genres AS g ON c.movie_id = g.movie_ID
      WHERE g.genre = 'Drama' 
         AND c.type = 'writer' 
         AND p.name NOT IN (
            SELECT p.name 
               FROM people AS p 
               INNER JOIN credits AS c ON p.id = c.person_id AND c.type = 'writer'
               INNER JOIN genres AS g ON c.movie_id = g.movie_id AND g.genre <> 'Drama'
         )
   
   GROUP BY p.name
   ORDER BY total DESC, p.name
   LIMIT 10
      
                                                                                                            
                                                                                                        
