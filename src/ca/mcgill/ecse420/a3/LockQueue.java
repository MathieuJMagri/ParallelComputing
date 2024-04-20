package ca.mcgill.ecse420.a3;

import java.util.Random;
import java.util.concurrent.locks.*;

public class LockQueue<E> {


  int headPointer;
  int tailPointer;

  int max; // Total size available

  ReentrantLock enqueue; // Lock for enqueue
  ReentrantLock dequeue; // Lock for dequeue

  Condition notEC; // Not empty condition
  Condition notFC; // Not full condition

  E[] array;

  // Constructor
  public LockQueue(int max) {
    this.headPointer = 0;
    this.tailPointer = this.headPointer;
    this.max = max;
    this.enqueue = new ReentrantLock();
    this.dequeue = new ReentrantLock();
    this.notEC = enqueue.newCondition();
    this.notFC = dequeue.newCondition();
    this.array = (E[]) new Object[this.max];
  }

  public void enqeue(E value) throws Exception {

    if (value == null) {
      throw new NullPointerException();
    }

    boolean wakeUp = false;
    enqueue.lock();
    try {
      while (max + headPointer == tailPointer) {
        notFC.await(); // making sure we are inbounds
      }

      array[tailPointer % max] = value;
      tailPointer++;
      if (1 + headPointer == tailPointer) { // When we are no longer inbounds, wakeUp
        wakeUp = true;
      }

    } finally {
      enqueue.unlock();
    }
    if (wakeUp == true) {
      dequeue.lock();
      try {
        notEC.signalAll();
      } finally {
        dequeue.unlock();
      }
    }
  }

  public E dequeue() throws Exception {
    boolean wakeUp = false;
    dequeue.lock();
    E newArray;
    try {
      while (headPointer == tailPointer) {
        notFC.await();
      }
      newArray = array[headPointer % max];
      headPointer++;
      if (max + headPointer == tailPointer) {
        wakeUp = true;
      }

    } finally {
      dequeue.unlock();
    }

    if (wakeUp == true) {
      enqueue.lock();
      try {
        notFC.signalAll();
      } finally {
        enqueue.unlock();
      }
    }
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

