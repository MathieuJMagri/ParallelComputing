package ca.mcgill.ecse420.a1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockProgram {
   // Create locks
   public static Lock lock1 = new ReentrantLock();
   public static Lock lock2 = new ReentrantLock();
   public static Integer balance = 0;

   public static void main(String args[]) {
      // Create threads
      Thread1 t1 = new Thread1();
      Thread2 t2 = new Thread2();
      // Start threads
      t1.start();
      t2.start();
   }

   private static class Thread1 extends Thread {
      public void run() {
         lock1.lock();
         System.out.println("t1: Holding lock 1");
         try {
            Thread.sleep(1);
            for (int i = 0; i < 100; i++) {
               balance += 1;
            }
            System.out.println(balance);
            System.out.println("t1: Releasing lock 1");
            // lock1.unlock(); //LINE TO UNCOMMENT TO STOP THE DEADLOCK
         } catch (InterruptedException e) {
         }
         System.out.println("t1: Waiting for lock 2");

         lock2.lock();
         System.out.println("t1: Holding lock 1 and lock 2");

      }
   }

   private static class Thread2 extends Thread {
      public void run() {
         lock2.lock();
         System.out.println("t2: Holding lock 2");

         try {
            Thread.sleep(1);
            for (int i = 0; i < 100; i++) {
               balance -= 1;
            }
            System.out.println(balance);
            System.out.println("t2: Releasing lock 2");
            // lock2.unlock(); //LINE TO UNCOMMENT TO STOP THE DEADLOCK
         } catch (InterruptedException e) {
         }
         System.out.println("t2: Waiting for lock 1");

         lock1.lock();
         System.out.println("t2: Holding lock 1 and lock 2");
      }

   }
}
