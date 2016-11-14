package uk.ac.cam.tma33.fjava.tick3star;

/**
 * OneLockConcurrentQueue.java
 *
 *    Queue using only 1 lock for offer/ poll
 *
 * Created by © Tudor Avram on 14/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class OneLockConcurrentQueue<T> implements ConcurrentQueue<T> {

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
   }

   private Link<T> first = null;
   private Link<T> last = null;

   /**
    *       Adds a new message to the queue
    *
    * @param message    The message to be added
    */

   @Override
   public synchronized void offer(T message) {
      Link<T> nextLink = new Link<T>(message);
      if (first == null) first = nextLink;
      else last.next = nextLink;
      last = nextLink;
      this.notify();
   }

   /**
    *       Gets the message from the head of the
    *          queue and pops the head off
    *
    * @return     The message from the head of the queue
    */

   @Override
   public synchronized T poll() {
      if (first == null) return null;

      T result = first.val;
      first = first.next;

      return result;
   }
}
