package uk.ac.cam.tma33.fjava.tick3star;

/**
 * TwoLockConcurrentQueue.java
 *
 *             Concurrency-safe queue implementation
 *    using 2 locks : one for the head and one for the tail
 *
 * Created by © Tudor Avram on 14/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class TwoLockConcurrentQueue<T> implements ConcurrentQueue<T> {

   /**
    *       Inner class representing the linked list used
    *                to maintain the queue
    */

   private static class Link<L> {
      L val;
      Link<L> next;

      /**
       *       Constructor
       *
       * @param val     the value to be added to the new Link
       */
      Link (L val) {
         this.val = val;
         this.next = null;
      }

      /**
       *      CONSTRUCTOR
       */


      Link () {
         this.next = null;
      }
   }

   private Link<T> first = null;
   private Link<T> last = null;

   private Object lockTL = new Object();
   private Object lockHD = new Object();

   /**
    *       CONSTRUCTOR
    */

   public TwoLockConcurrentQueue() {
      first = last = new Link<T>();
   }

   /**
    *       Adds a new message to the queue
    *
    * @param message    The message to be added
    */

   @Override
   public void offer(T message) {
      Link<T> nextLink = new Link<T>(message);
      synchronized (lockTL) {
         last.next = nextLink;
         last = nextLink;
      }
   }

   /**
    *       Gets the message from the head of the
    *          queue and pops the head off
    *
    * @return     The message from the head of the queue
    */

   @Override
   public T poll() {

      synchronized (lockHD) {
         Link<T> newFirst = first.next;
         if (newFirst == null) return null;

         T result = newFirst.val;
         first = newFirst;

         return result;
      }

   }

}
