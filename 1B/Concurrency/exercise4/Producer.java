package exercise4;

import java.util.ArrayList;

/**
 * Producer.java
 *
 *    Producer class used in e4.a
 *
 * Created by © Tudor Avram on 30/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class Producer extends Thread{

   private static final int MAXSIZE = 5;
   private ArrayList<Integer> mList;

   /**
    *          CONSTRUCTOR
    *
    *  @param list   The shared ArrayList used by both the producer and consumer
    */
   public Producer(ArrayList<Integer> list) {
      super();
      this.mList = list;
   }

   /**
    *    Method that adds an item to the list, as long as there is available space
    *
    * @param newItem       the element to be inserted
    */
   private synchronized void addItem(int newItem) {
      synchronized (mList) {

         while (mList.size() == MAXSIZE) {
            // we wait to make space in the list
            try {
               System.out.println("producer waiting");
               mList.wait();

            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }


            // adding the element
            mList.add(newItem);

            // notifying that we're done
            mList.notify();
         }
   }

   @Override
   public void run() {
      System.out.println("Starting Producer");
      for (int i = 0; i < 625; i++) {
         this.addItem(i);
      }
      System.out.println("Producer done");
   }
}
