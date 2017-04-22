package practice;

import uk.ac.cam.cl.databases.moviedb.MovieDB;
import uk.ac.cam.cl.databases.moviedb.model.*;

import java.util.TreeMap;
import java.util.Map;

/**
 * CountByGenre.java
 *
 *    Command line app that returns the count of the movies of each genre for
 * a given actor
 *
 * Created by © Tudor Avram on 21/04/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class CountByGenre {

   public static void main(String args[]) {
      if (args.length != 2) {
         System.err.println("Expected format: CountByGenre <database_path> <actor_name_prefix>");
      } else {
         try (MovieDB database = MovieDB.open(args[0])) {
            String prefix = args[1];

            // Getting all actors with that prefix
            Iterable<Person> actors = database.getByNamePrefix(prefix);
            for (Person person: actors) {
               TreeMap<String, Integer> genreCount = new TreeMap<>();

               // Getting all the roles for this actor
               Iterable<Role> roles = person.getActorIn();

               for (Role role: roles) {
                  int movieID = role.getMovieId();
                  Movie movie = database.getMovieById(movieID);


                  // Getting all the genres for this movie
                  Iterable<String> genres = movie.getGenres();
                  for (String genre:genres) {
                     if (genreCount.containsKey(genre)) {
                        genreCount.put(
                                genre, genreCount.get(genre) + 1
                        );
                     } else {
                        genreCount.put(
                                genre, 1
                        );
                     }
                  }
               }

               // Printing the result:
               System.out.println("For " + person.getName() + " : ");
               for (Map.Entry<String, Integer> entry: genreCount.entrySet()) {
                  System.out.println("   " + entry.getKey() + " : " + entry.getValue());
               }

               System.out.println();
            }
         }
      }
   }

}
