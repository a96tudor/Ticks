package uk.ac.cam.tma33.fjava.tick3;

/**
 * UnsafeMessageQueue.java
 *
 *    Unsafe implementation of a messaage queue for the server-side
 *    of a chat. DOES NOT perform any thread-safe implementation.
 *
 * Created by © Tudor Avram on 29/10/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class UnsafeMessageQueue<T> implements MessageQueue<T> {


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
    *       updateFirst() method
    *
    *    Updates the linked list
    *
    * @param newLink    The new link to be added to the list
    */

   private void updateFirst(Link<T> newLink) {
      if (first!=null) {
         last.next = newLink;
         last = newLink;
      } else {
         first = newLink;
         last = newLink;
      }
   }

   /**
    *         put() method
    *
    *       Adds a new element at the tail of the queue
    *
    * @param msg     The message to be inserted into the queue
    */

   @Override
   public void put(T msg) {
      Link<T> newLink = new Link<T>(msg);
      updateFirst(newLink);
   }

   /**
    *    take() method
    *
    *    Returns the first element from the queue
    *    If there isn't any, it blocks until one becomes available.
    *
    * @return     the first message from the queue
    */

   @Override
   public T take() {
      while (first == null) {
         try {
            Thread.sleep(100);
         } catch (InterruptedException ie) {
            // DO NOTHING
         }
      }

      T msg = first.val;      // getting the message
      first = first.next;     // updating first
      return msg;             // returning the message
   }

}
