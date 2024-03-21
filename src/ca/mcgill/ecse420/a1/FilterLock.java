package ca.mcgill.ecse420.a1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class FilterLock implements Lock {
    private static long[] level; // level[i] for thread i
    private static long[] victim; // victim[L] for level L
    private static int numThreads = 5; //

    public static void main(String args[]) {
        // call some stuff
        System.out.println("Starting the lock");
        FilterLock filter = new FilterLock(numThreads);
        filter.lock();
        filter.unlock();
        System.out.println("done");

        // TEST HERE
        FilterLock filterLock = new FilterLock(numThreads);
        testLock(filterLock, numThreads);
    }

    /*
     * FilterLock constructor
     * 
     * @param int n = size of level and victim lists
     */
    public FilterLock(int n) {
        level = new long[n];
        victim = new long[n];
        for (int i = 1; i < n; i++) {
            level[i] = 0;
        }
    }

    @Override
    public void lock() {
        // int i = ConcurrencyUtils.getCurrentThreadId();
        long i = Thread.currentThread().threadId();
        for (int L = 1; L < numThreads; L++) { // go through each level ( 1 to n - 1 )
            level[(int) i] = L; // announce intention to enter level L
            victim[L] = i; // thread sets itself as the victim at that level L
            /*
             * busy wait until there is no other thread in the same or higher level AND current
             * thread is the victim
             */
            for (int k = 0; k < numThreads; k++) {
                while ((k != i) && (level[k] >= L && victim[L] == i)) {
                } ;
            }
        }
    }

    @Override
    public void unlock() {
        long i = Thread.currentThread().threadId();
        level[(int) i] = 0;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'lockInterruptibly'");
    }

    @Override
    public boolean tryLock() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tryLock'");
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tryLock'");
    }

    @Override
    public Condition newCondition() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'newCondition'");
    }

    // Test method to verify n-thread mutual exclusion
    public static void testLock(FilterLock lock, int numThreads) {
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
