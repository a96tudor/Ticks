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

   private int mThreadNo;
   private boolean mPrintThreadNo;

   /**
    *       CONSTRUCTOR
    * @param threadNo      The thread number
    * @param printThreadNo    if we want the thread number to be printed or not
    */
   public HelloWorldThread(int threadNo, boolean printThreadNo) {
      super();
      this.mThreadNo = threadNo;
      this.mPrintThreadNo = printThreadNo;
   }

   /**
    *    Method called to run the thread
    */
   @Override
   public void run() {
      if (this.mPrintThreadNo) {
         runAndPrintThreadNo();
      } else {
         System.out.print("Hello world!");
      }
   }

   /**
    *    Method called to run the thread. It also prints the thread number
    */
   private void runAndPrintThreadNo() {
      System.out.print(this.mThreadNo + ". Hello world!");
   }

}
