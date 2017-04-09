SELECT p.name, count(*) AS total
      FROM people AS p 
      INNER JOIN credits AS c ON p.id = c.person_id 
      INNER JOIN genres AS g ON c.movie_id = g.movie_ID
          WHERE g.genre = 'Drama' AND c.type = 'writer'
  GROUP BY p.name
  ORDER BY total desc, p.name
  LIMIT 10;
                     
