package uk.ac.cam.tma33.fjava.tick2;

/**
 * EmptyNikException.java
 *
 *       Exception thrown if there was an empty nik provided.
 *
 * Created by © Tudor Avram on 23/10/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class EmptyNikException extends Exception {

   public EmptyNikException() {
      super(Strings.getEmptyNikMessage());
   }
}
