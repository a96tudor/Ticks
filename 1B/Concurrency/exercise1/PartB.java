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

      HelloWorldThread thread1 = new HelloWorldThread(1);
      HelloWorldThread thread2 = new HelloWorldThread(2);
      HelloWorldThread thread3 = new HelloWorldThread(3);
      HelloWorldThread thread4 = new HelloWorldThread(4);
      HelloWorldThread thread5 = new HelloWorldThread(5);

      thread1.run();
      thread2.run();
      thread3.run();
      thread4.run();
      thread5.run();

   }

}
