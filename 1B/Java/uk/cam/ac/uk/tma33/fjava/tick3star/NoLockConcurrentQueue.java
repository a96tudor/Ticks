package uk.ac.cam.tma33.fjava.tick3star;

import java.util.concurrent.atomic.AtomicReference;

/**
 * NoLockConcurrentQueue.java
 *
 *    Concurrent-safe Queue using no locks
 *    Uses the AtomicReference class instead
 *
 * Created by © Tudor Avram on 14/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class NoLockConcurrentQueue<T> implements ConcurrentQueue<T>{

   /**
    *       Inner class representing the linked list used
    *                to maintain the queue
    */

   private static class Link<L> {
      L val;
      AtomicReference<Link<L>> next;

      /**
       *       Constructor
       *
       * @param val     the value to be added to the new Link
       */
      Link (L val) {
         this.val = val;
         this.next = new AtomicReference<>(null);
      }

      /**
       *       CONSTRUCTOR
       */
      Link() {
         this.next = new AtomicReference<>(null);
      }
   }

   private AtomicReference<Link<T>> first = new AtomicReference<>(null);
   private AtomicReference<Link<T>> last = new AtomicReference<>(null);

   /** CONSTRUCTOR
    *
    */

   public NoLockConcurrentQueue() {
      Link<T> newNode = new Link<>();
      first.set(newNode);
      last.set(newNode);
   }

   /**
    *       Adds a new message to the queue
    *
    * @param message    The message to be added
    */

   @Override
   public void offer(T message) {
      Link<T> nextLink = new Link<T>(message);
      Link<T> tail;
      while (true) {
         tail = last.get();
         Link<T> next = tail.next.get();
         if (tail == last.get()) {
            if (next == null) {
               if (tail.next.compareAndSet(next, nextLink))
                  break;
            } else {
               last.compareAndSet(tail, next);
            }
         }
      }
      last.compareAndSet(tail, nextLink);
   }

   /**
    *       Gets the message from the head of the
    *          queue and pops the head off
    *
    * @return     The message from the head of the queue
    */

   @Override
   public T poll() {
      while (true) {
         Link<T> head = first.get();
         Link<T> tail = last.get();
         Link<T> next = head.next.get();
         if (head == first.get()) {
            if (head == tail) {
               if (next == null) return null;
               last.compareAndSet(tail, next);
            } else {
               T result = next.val;
               if (first.compareAndSet(head, next)) return result;
            }
         }
      }
   }

 }
