package ca.mcgill.ecse420.a3;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.*;

public class FreeQueue<E> {

  AtomicInteger headPointer;
  AtomicInteger tailPointer;
  AtomicInteger s; // Size

  int max; // Total size available

  // NO NEED FOR THESE ANYMORE
  // ReentrantLock enqueue; // Lock for enqueue
  // ReentrantLock dequeue; // Lock for dequeue
  //
  // Condition notEC; // Not empty condition
  // Condition notFC; // Not full condition

  E[] array;

  // Constructor
  public FreeQueue(int max) {
    this.headPointer = new AtomicInteger(0);
    this.tailPointer = this.headPointer;
    this.max = max;
    this.s = new AtomicInteger(0);
    // NO NEED FOR THESE ANYMORE
    // this.enqueue = new ReentrantLock();
    // this.dequeue = new ReentrantLock();
    // this.notEC = enqueue.newCondition();
    // this.notFC = dequeue.newCondition();
    this.array = (E[]) new Object[this.max];
  }

  public void enqueue(E value) throws Exception {

    while (s.get() == max || !this.s.compareAndSet(s.get(), s.get() + 1)) {

    }

    int tailValue = tailPointer.getAndIncrement();

    array[tailValue % max] = value;

    while (tailPointer.compareAndSet(tailValue, tailValue + 1)) {

    }

  }

  public E dequeue() throws Exception {

    int headValue = headPointer.getAndIncrement();
    E newArray;
    newArray = array[headValue % max];
    s.incrementAndGet();
    return newArray;

  }

  // Helper functions to generate random matrixes and vectors of 4000 in lenght

  // Generating a Random Vector
  public static double[] RandomVector(int lenght) {
    Random generator = new Random();
    double[] generatedVector = new double[lenght];
    for (int i = 0; i < lenght; i++) {
      generatedVector[i] = generator.nextDouble();
    }
    return generatedVector;
  }

  // Generating a Random Matrix
  public static double[][] RandomMatrix(int lenght) {
    Random generator = new Random();
    double[][] generatedMatrix = new double[lenght][lenght];
    for (int i = 0; i < lenght; i++) {
      for (int k = 0; k < lenght; k++) {
        generatedMatrix[i][k] = generator.nextDouble();
      }
    }
    return generatedMatrix;
  }

}
