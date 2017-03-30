package exercise2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * PartB.java
 *
 *    Exercise:    Now modify the program to use "synchronized" to ensure that increments
 *           on the shared variable are atomic
 *
 * Created by © Tudor Avram on 30/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class PartB {

   static AtomicInteger number = new AtomicInteger(0);

   static void incrementNumber() {
      number.incrementAndGet();
   }

   public static void main(String args[]) throws InterruptedException {



      // setting up thread1

      Thread thread1 = new Thread() {
         @Override
         public void run() {
            for (int i=0; i < 1000000; i++) {
               incrementNumber();
            }
         }
      };

      // setting up thread 2
      Thread thread2 = new Thread() {
         @Override
         public void run() {
            for (int i=0; i < 1000000; i++) {
               incrementNumber();
            }
         }
      };

      thread1.start();
      thread2.start();

      thread1.join();
      thread2.join();

      // printing the result
      System.out.println(number.get());
   }

}
