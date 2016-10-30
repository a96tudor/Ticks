package uk.ac.cam.tma33.fjava.tick2;

/**
 * InvalidCommandException.java
 *
 *       Exception thrown if there is an invalid command
 *
 * Created by © Tudor Avram on 23/10/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class InvalidCommandException extends Exception{

   public InvalidCommandException(String command) {
      super(Strings.getInvalidCommandMessage(command));
   }

}
