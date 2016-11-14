package uk.ac.cam.tma33.fjava.tick5;

/**
 * MessageQueue.java
 *
 *       A FIFO queue of items of type T
 *
 * Created by © Tudor Avram on 29/10/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public interface MessageQueue<T> {

   public abstract void put(T msg); // place a message on the end of the queue

   public abstract T take(); // block until lng > 0, return head of the queue

}
