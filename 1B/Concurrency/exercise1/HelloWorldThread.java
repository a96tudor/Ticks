package exercise1;

/**
 * HelloWorldThread.java
 *
 *    A thread that prints "Hello world!" when it is run
 *
 * Created by © Tudor Avram on 29/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class HelloWorldThread implements Runnable{

   private int threadNo;

   /**
    *       CONSTRUCTOR
    * @param threadNo      The threadnumber
    */
   public HelloWorldThread(int threadNo) {
      super();
      this.threadNo = threadNo;
   }

   /**
    *    Method called to run the thread
    */
   @Override
   public void run() {
      System.out.print("Hello world!");
   }

   /**
    *    Method called to run the thread. It also prints the thread number
    */
   public void runAndPrintThreadNo() {
      System.out.print(this.threadNo + ". Hello world!");
   }

}
