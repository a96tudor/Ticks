package uk.ac.cam.tma33.fjava.tick3;

/**
 *       Class that simulates a bank environment
 *
 *       implementation from : http://www.cl.cam.ac.uk/teaching/1617/FJava/BankSimulator.java
 */

import java.util.Random;

public class BankSimulator {

   /**
    *       Inner class, representing a bank account
    */
   private class BankAccount {
      private int balance;    // current balance in the account
      private int acc;        // the account number

      /**
       *          CONSTRUCTOR
       * @param accountNumber    the account number of the new account
       * @param deposit          the initial deposit for the new account
       */
      BankAccount(int accountNumber, int deposit) {
         balance = deposit;
         acc = accountNumber;
      }

      /**
       *       getAccountNumber() method
       * @return     the account number for a given instance of the class
       */
      public int getAccountNumber() {
         return acc;
      }

      /**
       *             transferTo() method
       *
       *       Transfers $amount from this to b
       *
       * @param b          The account to transfer to
       * @param amount     The amount to transfer
       */

      public void transferTo(BankAccount b, int amount) {
         if (b.acc < this.acc) {
            synchronized (b) {
               synchronized (this) {
                  balance -= amount;
                  b.balance += amount;
               }
            }
         } else {
            synchronized (this) {
               synchronized (b) {
                  balance -= amount;
                  b.balance += amount;
               }
            }
         }

      }
   }

   private static Random r = new Random();

   /**
    *       inner class representing a teller
    */
   private class RoboTeller extends Thread {
      public void run() {
         //Robots work from 9am until 5pm; one customer per second
         for(int i=9*60*60; i<17*60*60; i++) {
            int a = r.nextInt(account.length);
            int b = r.nextInt(account.length);
            account[a].transferTo(account[b], r.nextInt(100));
         }
      }
   }

   private int capital;                // the total capital from the bank
   private BankAccount[] account;      // the list of accounts
   private RoboTeller[] teller;        // the list of tellers

   /**
    *                CONSTRUCTOR
    *
    * @param capital       The initial capital from the bank
    * @param accounts      The initial number of accounts
    * @param tellers       The initial number of tellers
    */

   public BankSimulator(int capital, int accounts, int tellers) {
      this.capital = capital;
      this.account = new BankAccount[accounts];
      this.teller = new RoboTeller[tellers];
      for(int i=0; i<account.length; i++)
         account[i]  = new BankAccount(i,capital/account.length);
   }

   /**
    *       getCapital() method
    *
    * @return        The total capital available in the bank at a certain time
    */
   public int getCapital() {
      return capital;
   }

   /**
    *          runDay() method
    *
    *      Runs one day's simulation in order to check the reliability of the implementation
    */

   public void runDay() {
      for(int i=0; i<teller.length; i++)
         teller[i] = new RoboTeller();
      for(int i=0; i<teller.length; i++)
         teller[i].start();

      int done = 0;
      while(done < teller.length)
         try{teller[done].join();done++;} catch(InterruptedException e) {}

      int finalCapital = 0;
      for(int i=0; i<account.length; i++)
         finalCapital += account[i].balance;
      capital = finalCapital;
   }

   public static void main(String[] args) {
      BankSimulator javaBank = new BankSimulator(10000,10,100);
      javaBank.runDay();
      System.out.println("Capital at close: Â£"+javaBank.getCapital());
   }
}