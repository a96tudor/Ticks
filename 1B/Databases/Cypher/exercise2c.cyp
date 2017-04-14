//Let's build a simple recommendation engine. Starting from a known movie that you liked, write a query to finds similar movies that you might also enjoy.

//Let A be the movie that you liked. Some other movie B should be considered for recommendation if A and B have at least one genre in common, at least one keyword in common, and at least one actor in common. Furthermore, you can calculate a similarity score as follows: 1 point for every keyword that A and B have in common, and 10 points for every actor the movies have in common.

//Write a query that produces recommendations for someone who liked Skyfall (2012). The results should give the title of each recommended movie and the similarity score, and be sorted by score:

match (liked:Movie)<-[:ACTS_IN]-(actor:Person)-[:ACTS_IN]->(rec:Movie),
      (liked)-[:HAS_GENRE]->()<-[:HAS_GENRE]-(rec),
      (liked)-[:HAS_KEYWORD]->(keyword:Keyword)<-[:HAS_KEYWORD]-(rec)
where liked.title = 'Skyfall (2012)'      
with rec,
     count(distinct actor) as actors_count,
     count(distinct keyword) as keywords_count
return rec.title as title, 10*actors_count + keywords_count as score
order by score desc

