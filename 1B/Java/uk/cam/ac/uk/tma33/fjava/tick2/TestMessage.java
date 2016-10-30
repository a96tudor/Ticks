package uk.ac.cam.tma33.fjava.tick2;

import java.io.Serializable;

/**
 * TestMessage.java
 *
 * Class used for testing  serialisation
 *
 * Created by © Tudor Avram on 14/10/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */

public class TestMessage implements Serializable{

   private static final long serialVersionUID = 1L;
   private String text;

   public String getMessage() {
      return text;
   }

   public void setMessage(String msg) {
      text = msg;
   }

}
