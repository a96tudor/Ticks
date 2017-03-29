package exercise1;

/**
 * PartA.java
 *
 *    Exercise:   Write a short program that prints "Hello world" from an additional
 *              thread using the Java Thread API.
 *
 * Created by © Tudor Avram on 29/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */

public class PartA {

   public static void main(String args[]) {

      Thread myThread = new Thread(new HelloWorldThread(0, false));
      myThread.run();

   }

}

