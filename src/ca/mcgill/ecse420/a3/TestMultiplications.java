package ca.mcgill.ecse420.a3;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class TestMultiplications {

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

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    int length = 8000;
    double[] V = RandomVector(length);
    double[][] M = RandomMatrix(length);

    long seqStartTime;
    long seqEndTime;

    long paraStartTime;
    long paraEndTime;

    long Time1; // Time for Sequential Multiplication
    long Time2; // Time for Parallel Multiplication

    double[] seqResult;
    double[] paraResult;

    seqStartTime = System.currentTimeMillis();
    seqResult = SequentialMatrixMultiplication.SeqMatrixMultiplication(V, M);
    seqEndTime = System.currentTimeMillis();
    Time1 = seqEndTime - seqStartTime;

    paraStartTime = System.currentTimeMillis();
    paraResult = ParallelMatrixMultiplication.ParaMatrixMultiplication(V, M);
    paraEndTime = System.currentTimeMillis();
    Time2 = paraEndTime - paraStartTime;


    System.out.println("Time for the Sequential Matrix Vector Multiplication: " + Time1);
    System.out.println("Time for the Parallel Matrix Vector Multiplication: " + Time2);

    // SpeedUp Calculation is 1 thread time over n thread time, in this case we have 4 threads
    double speedUp = (double) (Time1 / Time2);
    System.out.println("SpeedUp: " + speedUp);

    // double speedup = (double) sequentialTime / parallelTime;


  }

}
