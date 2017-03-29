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

      HelloWorldThread thread1 = new HelloWorldThread(1);
      HelloWorldThread thread2 = new HelloWorldThread(2);
      HelloWorldThread thread3 = new HelloWorldThread(3);
      HelloWorldThread thread4 = new HelloWorldThread(4);
      HelloWorldThread thread5 = new HelloWorldThread(5);

      thread1.runAndPrintThreadNo();
      thread2.runAndPrintThreadNo();
      thread3.runAndPrintThreadNo();
      thread4.runAndPrintThreadNo();
      thread5.runAndPrintThreadNo();

   }
   
}
