package exercise3;

import java.util.concurrent.Semaphore;

/**
 * PartB.java
 * TODO : complete with functionality
 * <p>
 * Created by © Tudor Avram on 30/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class PartB {

   private static int val = 0;
   private static boolean done;
   private static Semaphore semaphore = new Semaphore(1);

   public static void main(String args[]) throws InterruptedException {

      // incrementing thread
      Thread incThread = new Thread() {

         @Override
         public void run() {
            try {
               semaphore.acquire();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }

            for (int i=0; i < 1000000; i++) {
               val++;
            }
            
            semaphore.release();
         }

      };

      //printing thread
      Thread printThread = new Thread() {

         @Override
         public void run() {
            while(!semaphore.tryAcquire());
            System.out.print(val);
         }
      };

      // running threads
      incThread.start();
      printThread.start();
   }

}
