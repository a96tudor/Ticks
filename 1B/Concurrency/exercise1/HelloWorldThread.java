package exercise1;

/**
 * HelloWorldThread.java
 * TODO : complete with functionality
 * <p>
 * Created by © Tudor Avram on 29/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class HelloWorldThread extends Thread{

   private int threadNo;

   /**
    *       CONSTRUCTOR
    * @param threadNo      The threadnumber
    */
   public HelloWorldThread(int threadNo) {
      super();
      this.threadNo = threadNo;
   }

   @Override
   public void run() {
      System.out.print("Hello world!");
      super.run();
   }

   public void runAndPrintThreadNo() {
      System.out.print(this.threadNo + ". Hello world!");
      super.run();
   }

}
