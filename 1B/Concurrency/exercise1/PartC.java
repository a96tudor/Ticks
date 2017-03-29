package exercise1;

/**
 * PartC.java
 *
 *    Exercise: Now modify the printed string to include the thread number; ensure that
         all threads have a unique thread number.
 *
 * Created by © Tudor Avram on 29/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class PartC {

   public static void main(String args[]) {

      Thread thread1 = new Thread(new HelloWorldThread(1, true));
      Thread thread2 = new Thread(new HelloWorldThread(2, true));
      Thread thread3 = new Thread(new HelloWorldThread(3, true));
      Thread thread4 = new Thread(new HelloWorldThread(4, true));
      Thread thread5 = new Thread(new HelloWorldThread(5, true));

      thread1.run();
      thread2.run();
      thread3.run();
      thread4.run();
      thread5.run();
   }
   
}
