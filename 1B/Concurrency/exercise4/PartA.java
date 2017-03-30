package exercise4;

import java.util.ArrayList;

/**
 * PartA.java
 *
 *    Exercise:   We have seen several examples of producer-consumer implemented using a
 *       number of different synchronisation primitives in pseudo-code.
 *       Implement a ProducerConsumer class using synchronized, wait(), and
 *       notify() in Java, and use it to pass a sequence of integer values from
 *       one thread (the producer) to a second that prints them (the consumer).
 *
 * Created by © Tudor Avram on 30/03/17.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class PartA {

   private static ArrayList<Integer> list = new ArrayList<>();

   public static void main(String args[]) {
      // setting up the 2 threads
      Producer p = new Producer(list);
      Consumer c = new Consumer(list);

      p.start();
      c.start();
   }

}
