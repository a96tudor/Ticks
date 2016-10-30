package uk.ac.cam.tma33.fjava.tick3;

/**
 *       QueueTest class
 *
 *            Shows the difference between the unsafe (i.e. not thread-safe) and the safe
 *      (i.e. thread-safe ) implementations of the message queue, by using a producer-consumer
 *      implementation and printing "#received_messages / #sent_messages"
 *
 *      implementation provided by : www.cl.cam.ac.uk/teaching/1617/Fjava/QueueTest.java
 */

public class QueueTest {

   /**
    *       Inner class, representing a producer
    */
   private class Producer extends Thread {
      private int sent = 0;      // number of produced items
      public void run() {
         for (int i = 0; i < 50000; ++i) {
            q.put("" + i);
            sent++;
         }
      }
      public int numberProduced() {return sent;}
   }

   /**
    *    Inner class, representing a consumer
    */
   private class Consumer extends Thread {
      private int recv = 0;      // number of received items
      public void run() {
         while (!q.take().equals("EOF")) {
            recv++;
         }
         q.put("EOF");
      }
      public int numberConsumed() {return recv;}
   }

   private MessageQueue<String> q;     // datastructure to store the messages
   private Consumer[] consumers;       // list of consumers
   private Producer[] producers;       // list of producers

   /**
    *          CONSTRUCTOR
    *
    * @param q    The message queue that must be used
    * @param c    The number of consumers
    * @param p    The number of producers
    */

   QueueTest(MessageQueue<String> q, int c, int p) {
      this.q = q;
      consumers = new Consumer[c];
      for (int i = 0; i < c; ++i)
         consumers[i] = new Consumer();
      producers = new Producer[p];
      for (int i = 0; i < p; ++i)
         producers[i] = new Producer();
   }

   /**
    *       run() method
    */

   public void run() {

      for (Consumer c : consumers) c.start();      // initiates every consumer
      for (Producer p : producers) p.start();      //  and producer available
      for (Producer p : producers)
         try {
            /*
                     join() --- method inherited from Thread.
                  Waits for every producer to die until it proceeds
                              to the next one.
             */
            p.join();
         } catch (InterruptedException e) {

         }

      q.put("EOF"); // adding the EOF message to the message queue, so the consumers would
                    // know where to stop
      //terminate join at 10 secs since EOF marker may get lost
      for (Consumer c : consumers)
         try {
            /*
                     join(milliseconds) --- also inherited from Thread
                  In this case, it forces the consumer to wait for at most 10s,
                     unlike what we did in the producers case, where we were
                           waiting until it finished, no matter how
                                      long it took.

             */
            c.join(10000);
         } catch (InterruptedException e) {

         }

      int recv = 0;        // number of received messages
      for (Consumer consumer : consumers) recv += consumer.numberConsumed();
      int sent = 0;        // number of sent messages
      for (Producer p : producers) sent += p.numberProduced();
      System.out.println(recv + " / " + sent);
   }

   public static void main(String[] args) {
     /* System.out.println("** UNSAFE ** ");
      new QueueTest(new UnsafeMessageQueue<String>(), 1, 1).run();   // printed 50000 / 50000
                                                                     // as expected, there is no concurrency when we
                                                                     // have only 1 producer & 1 consumer. Thus, no
                                                                     // concurrency and no thread fails :D

      new QueueTest(new UnsafeMessageQueue<String>(), 3, 1).run();   // printed 132694 / 50000
                                                                     // It does an odd thing :-??  reads more than it
                                                                     // actually writes.

      new QueueTest(new UnsafeMessageQueue<String>(), 1, 3).run();   // printed 303 / 150000
                                                                     // Thus, 150000 - 303 = 149697 reads failed

      new QueueTest(new UnsafeMessageQueue<String>(), 3, 3).run();   // printed 2716 / 1500000
                                                                     // Thus 150000 - 2716 = 147284 reads failed
      */
      System.out.println("** SAFE ** ");
      new QueueTest(new SafeMessageQueue<String>(), 1, 1).run();
      new QueueTest(new SafeMessageQueue<String>(), 3, 1).run();
      new QueueTest(new SafeMessageQueue<String>(), 1, 3).run();
      new QueueTest(new SafeMessageQueue<String>(), 3, 3).run();

      /*
       *          (1) RESULT AFTER 1st RUN : The implementation of SafeMessageQueue does not work.
       *          This might be the case that a certain thread is waiting for the another thread to finish,
       *          but it doesn't terminate either, causing the program to go into an infinite loop. In other
       *          words, we are in deadlock : If, when calling take(), the queue is empty, the thread goes to sleep
       *          until some data is available. The problem is the fact that the consumer holds the lock, so no
       *          producer can actually insert an item into the queue. Thus, the consumer will sleep "forever".
       *
       *          (2) RESULT AFTER 2nd RUN : Printed
       *                         50000 / 50000
                                 50000 / 50000
                                 150000 / 150000
                                 150000 / 150000
                      As expected.
       */
   }
}