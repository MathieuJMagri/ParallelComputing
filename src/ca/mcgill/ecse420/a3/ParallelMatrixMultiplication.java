package ca.mcgill.ecse420.a3;

import java.util.Random;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;


public class ParallelMatrixMultiplication {

  // This Parallel Matrix Vector Multiplication is very much inspired from manual notes and class
  // notes in Chapter 16
  public static double[] ParaMatrixMultiplication(double[] v, double[][] m)
      throws ExecutionException, InterruptedException {

    // Create Thread Pool
    ExecutorService executor = Executors.newCachedThreadPool();

    // Matrix values/dimensions
    int r = m.length; // rows
    double[] multiplication = new double[r]; // setting up result matrix
    int c = m[0].length; // columns

    // 4 threads is purely based on the machine I am using
    int threads = 4;
    int size = r / threads; // size of a block for each thread

    // Using the Future interface as mentioned in the assignment handout and chapter 16 of the
    // notes/manual.
    List<Future<Void>> fut = new ArrayList<>();

    for (int i = 0; i < r; i += size) {
      int start = i;
      int end;
      if (i + size <= r) {
        end = i + size;
      } else {
        end = r;
      }

      // Same concept as the sequential multiplication except that the task is now divided up in a
      // bunch
      // of smaller tasks and we use Callable.
      Callable<Void> task = () -> {
        for (int l = start; l < end; l++) {
          double addition = 0;
          for (int f = 0; f < r; f++) {
            addition += m[l][f] * v[f];
          }
          multiplication[l] = addition;
        }
        return null;
      };
      fut.add(executor.submit(task));
    }

    // wait for the results to finish being computed
    for (Future<Void> future : fut) {
      future.get();
    }

    executor.shutdown();
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    return multiplication;

  }

}
