// Exercise text: 
//Complete the following Cypher query so that it returns rows name, total where the name column is the name of an actor, and the total column indicates the number of movies in which they co-starred with Jennifer Lawrence. Note, here we want the total to always be greater than 1. (HINT: Consider using the WITH construct.)

//YOUR-CODE-GOES-HERE
//return name, total
//order by name, total;

match (n:Person)-[:ACTS_IN]->(:Movie)<-[:ACTS_IN]-(m:Person)
   where n.name <> m.name and n.name='Lawrence, Jennifer (III)'
   with m.name as name, count(*) as total
      where total > 1
   return name, total
   order by name, total
