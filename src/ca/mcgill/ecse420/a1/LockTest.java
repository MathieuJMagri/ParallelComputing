import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;

public class LockTest {

    private static final int NUM_THREADS = 10; // Number of threads
    private static AtomicInteger counter = new AtomicInteger(0); // Shared resource
    private static ReentrantLock lock = new ReentrantLock(); // Lock for mutual exclusion

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        // Create and start threads
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new WorkerThread());
            threads[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].join();
        }

        // Verify that the counter has been incremented exactly NUM_THREADS times
        if (counter.get() == NUM_THREADS) {
            System.out.println("Test passed, counter is " + counter.get());
        } else {
            System.out.println("Test failed, counter is " + counter.get());
        }
    }

    static class WorkerThread implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                int currentCount = counter.get();
                System.out.println("Thread " + Thread.currentThread().getName() +
                        " entering critical section with counter at " + currentCount);
                counter.incrementAndGet(); // Atomically increments by one
                System.out.println("Thread " + Thread.currentThread().getName() +
                        " exiting critical section with counter at " + counter.get());
            } finally {
                lock.unlock();
            }
        }
    }
}

