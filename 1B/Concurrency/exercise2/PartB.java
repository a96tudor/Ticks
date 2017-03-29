package exercise2;

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

   private static AtomicInteger number = new AtomicInteger(0);

   public static void main(String args[]) {

      // setting up thread1

      Thread thread1 = new Thread() {
         @Override
         public void run() {
            for (int i=0; i < 1000000; i++) {
               number.increment();
            }
         }
      };

      // setting up thread 2
      Thread thread2 = new Thread() {
         @Override
         public void run() {
            for (int i=0; i < 1000000; i++) {
               number.increment();
            }
         }
      };

      // running the two threads
      thread1.run();
      thread2.run();

      // printing the result
      number.print();
   }

}
