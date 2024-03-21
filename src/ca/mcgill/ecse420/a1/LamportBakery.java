package ca.mcgill.ecse420.a1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

// The following is inspired by the pseudocode explained in class and in the course manual.

public class LamportBakery implements Lock {
    private int n;
    private volatile int[] label;
    private volatile boolean[] flag;

    public static void main(String args[]) {
        // Test of lock and unlock:
        System.out.println("Starting the lock");
        LamportBakery lamportBakery = new LamportBakery(3);
        lamportBakery.lock();
        lamportBakery.unlock();
        System.out.println("done");

        // QUESTION 1.5 TEST HERE:
        int numThreads = 5;
        LamportBakery lamportBakeryTest = new LamportBakery(numThreads);
        testLock(lamportBakery, numThreads);
    }

    public LamportBakery(int n) {
        this.n = n;
        label = new int[n];
        flag = new boolean[n];

        for (int i = 0; i < n; i++) {
            flag[i] = false;
            label[i] = 0;
        }

    }


    @Override
    public void lock() {
        int i = (int) Thread.currentThread().getId() % n;
        flag[i] = true;
        label[i] = maximum(label) + 1;
        for (int k = 0; k < n; k++) {

            while ((k != i) && flag[k]
                    && ((label[k] < label[i]) || ((label[k] == label[i]) && k < i))) {
            }

        }


    }


    private int maximum(int[] label) {
        int max = -1;
        for (int i : label) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    @Override
    public void unlock() {
        flag[(int) Thread.currentThread().getId() % n] = false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        // TODO Auto-generated method stub

    }


    @Override
    public boolean tryLock() {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public Condition newCondition() {
        // TODO Auto-generated method stub
        return null;
    }

    // Test method to verify n-thread mutual exclusion
    public static void testLock(LamportBakery lock, int numThreads) {
        AtomicInteger counter = new AtomicInteger(0);

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                lock.lock();
                try {
                    int count = counter.incrementAndGet(); // Increment the shared counter
                    System.out.println("Thread " + Thread.currentThread().getId()
                            + " in critical section with count " + count);
                } finally {
                    lock.unlock();
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (counter.get() == numThreads) {
            System.out.println("Test passed, counter is " + counter.get());
        } else {
            System.out.println(
                    "Test failed, expected " + numThreads + " but counter is " + counter.get());
        }
    }



}
