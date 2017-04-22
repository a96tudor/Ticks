package practice;

import uk.ac.cam.cl.databases.moviedb.model.*;
import uk.ac.cam.cl.databases.moviedb.MovieDB;

/**
 * Main1.java
 *
 *    Just playing around with stuff
 *
 * Created by © Tudor Avram on 21/04/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class Main1 {

   private static Movie getMovieByID(MovieDB db, int id) {
      Movie result = db.getMovieById(id);
      return result;
   }

   private static Person getPersonByID(MovieDB db, int id) {
      Person result = db.getPersonById(id);
      return result;
   }

   private static Iterable<Person> getAllPeople(MovieDB db) {
      Iterable<Person> result = db.getByNamePrefix("");
      return result;
   }

   private static Iterable<Movie> getAllMovies(MovieDB db) {
      Iterable<Movie> result = db.getByTitlePrefix("");
      return result;
   }

   private static Iterable<Role> getAllRoles(Person person) {
      return person.getActorIn();
   }

   public static void main(String args[]) {
      try (MovieDB myDB = MovieDB.open(args[0])) {
         int id = Integer.parseInt(args[1]);
         System.out.println(getPersonByID(myDB, id));

         /*
            Getting all people and counting them
          */
         int count = 0;
         for (Person person : getAllPeople(myDB)) {
            count++;
         }
         System.out.println("TOTAL PEOPLE: " + count);

         /*
            Getting all movies and counting them
          */
         count = 0;
         for (Movie movie : getAllMovies(myDB)) {
            count++;
         }
         System.out.println("TOTAL MOVIES: " + count);

         /*
               Getting all movies for Matt Damon
          */
         for (Person person : myDB.getByNamePrefix("Damon, Matt")) {
            System.out.println(person.getName() + " played:");
            for (Role role : getAllRoles(person)) {
               System.out.println("    " + role.getCharacter() + " in " + role.getTitle());
            }
         }
      }

   }
}
