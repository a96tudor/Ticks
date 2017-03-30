package exercise3;

/**
 * PartA.java
 *
 *    Exercise:   Write a short program in which one thread increments an integer
 *          1,000,000 times, and a second thread prints the integer -- without
 *          waiting for it to finish.
 *
 * Created by © Tudor Avram on 30/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class PartA {

   private static int val = 0;

   public static void main(String args[]) {

      // incrementing thread
      Thread incThread = new Thread() {

         @Override
         public void run() {
            for (int i=0; i < 1000000; i++) {
               val++;
            }
         }

      };

      //printing thread
      Thread printThread = new Thread() {

         @Override
         public void run() {
            System.out.print(val);
         }
      };

      // running threads
      incThread.start();
      printThread.start();
   }

}
