package uk.ac.cam.tma33.fjava.tick4;

import java.util.Set;
import java.util.HashSet;

/**
 * MultiQueue.java
 *
 *       A queue that stores all the clients (i.e. their message queues)
 *                    and accesses them directly.
 *
 * Created by © Tudor Avram on 05/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class MultiQueue<T> {

   private Set<MessageQueue<T>> outputs = new HashSet<MessageQueue<T>>(); // a set with all the clients that are
                                                                            // subscribed to the given server

   /**
    *       register() method
    *
    *    Registers a new message queue to the server
    * @param q    The message queue to be registered
    */

   public void register(MessageQueue<T> q) {
      synchronized (this) {
         outputs.add(q);
      }
   }

   /**
    *       deregister() methdd
    *
    *      Unsubscribes a message queue from the server
    *
    * @param q       The message queue to be removed from the multiqueue
    */
   public void deregister(MessageQueue<T> q) {
      synchronized (this) {
         outputs.remove(q);
      }
   }

   /**
    *    put() method
    *
    *     Adds a message to every message queue in the multi queue
    *
    * @param message       The message to pe put to the message queues
    */
   public void put(T message) {
      synchronized(this) {
         for (MessageQueue<T> o : outputs) {
            o.put(message);
         }
      }
   }

}
