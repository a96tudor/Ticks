//In the tutorial we computed the distance between Jennifer Lawrence and Matt Damon (using the ACTS_IN relationship). The distance between two actors A and B is 1 if they co-starred in the same movie; the distance is 2 if there is some other actor X such that A co-starred with X on some movie and X co-starred with B on some other movie; and so on.

//For this exercise we will compute the distance between all of our genres. The definition of distance is similar: for example, the distance between two genres is 1 if there is at least one movie that is associated with those two genres. We can use this distance as a measure of how similar two genres are.

match(g:Genre)
with g.genre as genre1
    match path = allshortestpaths( (g1:Genre)-[:HAS_GENRE*]-(g2:Genre) )
    where g1.genre <> g2.genre
      and g1.genre = genre1
return genre1, g2.genre as genre2, length(path)/2 as distance
order by distance desc, genre1, genre2

