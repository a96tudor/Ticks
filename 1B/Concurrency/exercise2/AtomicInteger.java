package exercise2;

/**
 * AtomicInteger.java
 *
 *    Class representing an atomic integer. It implements an synchronised method for incrementing the integer
 *
 * Created by © Tudor Avram on 30/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class AtomicInteger {

   private int mVal;

   /**
    *    CONSTRUCTOR
    * @param init    The initial value for the counter
    */
   public AtomicInteger(int init) {
      this.mVal = init;
   }

   /**
    *    Method that increments mVal
    */
   public synchronized void increment() {
      mVal++;
   }

   /**
    *    Method that prints the contents of the object
    */
   public void print() {
      System.out.print(mVal);
   }

}
