package exercise4;

import java.util.ArrayList;

/**
 * Consumer.java
 * TODO : complete with functionality
 * <p>
 * Created by © Tudor Avram on 30/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class Consumer extends Thread {

   private ArrayList<Integer> mList;

   /**
    * CONSTRUCTOR
    *
    * @param list The shared list used by both the Consumer and Producer
    */
   public Consumer(ArrayList<Integer> list) {
      super();
      this.mList = list;
   }

   /**
    * Method that:
    * - waits for an element to become available
    * - once it does, it is :
    * 1. printed to the console
    * 2. removed from the list
    */
   private synchronized void consume() {
      synchronized (mList) {
         while (mList.isEmpty()) {
            // we wait
            try {
               System.out.println("Consumer waiting");
               mList.wait();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
         // extracting and printing the value
            int val = this.mList.get(0);
            this.mList.remove(0);
            System.out.println(val);

            // notifying the other threads
            mList.notify();
      }
   }

   /**
    *    Method used when the thread is running
    */
   @Override
   public void run() {
      System.out.println("Starting Consumer");
      for (int i = 1; i <= 10000; i++) {
         if (i%16 == 0) {
            this.consume();
         }
      }
      System.out.println("Finished Consumer");
   }
}
