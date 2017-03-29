package exercise1;

/**
 * PartB.java
 *
 *    Exercise:   Now modify the program to print "Hello world" five times, once from each
 *              of five different threads.  Ensure that the strings are not interleaved
 *              in the output.
 *
 * Created by © Tudor Avram on 29/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class PartB {

   public static void main(String args[]) {

      Thread thread1 = new Thread(new HelloWorldThread(1, false));
      Thread thread2 = new Thread(new HelloWorldThread(2, false));
      Thread thread3 = new Thread(new HelloWorldThread(3, false));
      Thread thread4 = new Thread(new HelloWorldThread(4, false));
      Thread thread5 = new Thread(new HelloWorldThread(5, false));

      thread1.run();
      thread2.run();
      thread3.run();
      thread4.run();
      thread5.run();

   }

}
