package ca.mcgill.ecse420.a3;

import java.util.Random;

public class SequentialMatrixMultiplication {

  // SequentialMatrixMultiplication
  public static double[] SeqMatrixMultiplication(double[] v, double[][] m) {
    int r = m.length; // rows
    double[] multiplication = new double[r]; // setting up result matrix
    int c = m[0].length; // columns
    for (int i = 0; i < r; i++) {
      double addition = 0;
      for (int k = 0; k < c; k++) {
        addition += m[i][k] * v[k];
      }
      multiplication[i] = addition;
    }

    return multiplication;

  }


}

