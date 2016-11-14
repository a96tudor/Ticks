package uk.ac.cam.tma33.fjava.tick3star;

/**
 * ConcurrentQueue.java
 *
 * An interface representing a concurrent queue
 *
 * Created by © Tudor Avram on 14/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public interface ConcurrentQueue<T> {

   public void offer(T message); //Add "message" to queue
   public T poll(); //Return first item from queue or null if empty

}
