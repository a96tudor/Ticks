package exercise2;

/**
 * PartA.java
 *
 *    Exercise:    Write a short program in which two threads both increment a shared
 *           integer repeatedly, without proper synchronisation, 1,000,000 times,
 *           printing the resulting value at the end of the program.  Run the program
 *           on a multicore system and attempt to exercise the potential race in the
 *           program.
 *
 * Created by © Tudor Avram on 29/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class PartA {

   private static int number = 0;

   public static void main(String args[]) {

      // setting up thread 1
      Thread thread1 = new Thread() {
         @Override
         public void run() {
            for (int i=0; i < 1000000; i++) {
               number++;
            }
         }
      };

      // setting up thread 2
      Thread thread2 = new Thread() {
         @Override
         public void run() {
            for (int i=0; i < 1000000; i++) {
               number++;
            }
         }
      };
      
      // running the threads
      thread1.run();
      thread2.run();

      // printing the result
      System.out.print(number);
   }

}
