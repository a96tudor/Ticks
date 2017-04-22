package tick;

import uk.ac.cam.cl.databases.moviedb.MovieDB;
import uk.ac.cam.cl.databases.moviedb.model.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Exercise3.java
 * TODO : complete with functionality
 * <p>
 * Created by © Tudor Avram on 21/04/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class Exercise3 {

   public static void main(String args[]) {
      if (args.length != 2) {
         System.err.println("Expected format: Exercise3 <database_path> <shared_position>");
      } else {
         try (MovieDB database = MovieDB.open(args[0])) {
            int position = Integer.parseInt(args[1]);

            // getting all the movies
            Iterable<Movie> movies = database.getByTitlePrefix("");

            for (Movie movie: movies) {
               ArrayList<String> actorsWithPosition = new ArrayList<>();

               // Getting all the credits
               Iterable<CreditActor> actors = movie.getActors();
               if (actors!=null) {
                  for (CreditActor actor : actors) {
                     Integer currentPosition = actor.getPosition();

                     if (currentPosition == null) {
                        continue;
                     }

                     if (actor.getPosition() > position) {
                        break;
                     } else {
                        if (actor.getPosition() == position) {
                           actorsWithPosition.add(actor.getName());
                        }
                     }

                  }
               }

               if (actorsWithPosition.size() == 2) {
                  Collections.sort(actorsWithPosition);
                  System.out.println(
                          actorsWithPosition.get(0) + " and " +
                          actorsWithPosition.get(1)  + " both have position " +
                          position + " in " + movie.getTitle()
                  );
               }
            }
         }
      }
   }

}
