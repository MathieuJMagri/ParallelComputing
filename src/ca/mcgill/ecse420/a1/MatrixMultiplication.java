package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MatrixMultiplication {

	private static final int NUMBER_THREADS = 1; // change this value to test different thread pool
													// sizes
	private static final int MATRIX_SIZE = 2000; // change this value to test different matrix sizes
													// reduce the value here below 5 to test the
													// matrix mult. algorithm

	public static void main(String[] args) {

		// Generate two random matrices, same size
		double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
		double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
		long startTime = System.currentTimeMillis();
		// double[][] seqMatrix = sequentialMultiplyMatrix(a, b);
		sequentialMultiplyMatrix(a, b);
		long endTime = System.currentTimeMillis();
		System.out.println("sequentialMultiplyMatrix run time: " + (endTime - startTime) + " ms");
		startTime = System.currentTimeMillis();
		// double[][] parMatrix = parallelMultiplyMatrix(a, b);
		parallelMultiplyMatrix(a, b);
		endTime = System.currentTimeMillis();
		System.out.println("parallelMultiplyMatrix run time: " + (endTime - startTime) + " ms");

		if (MATRIX_SIZE < 5) {
			// print the small matrixes for validation of the matrix multiplication:
			// uncomment lines 22,27,41 and 43. comment lines 23 and 28
			// do the inverse to test for execution times
			System.out.println("Matrix A");
			printMatrixTest(a);
			System.out.println("Matrix B");
			printMatrixTest(b);
			System.out.println("Sequential");
			// printMatrixTest(seqMatrix);
			System.out.println("Parallel");
			// printMatrixTest(parMatrix);
		}
	}

	/**
	 * Returns the result of a sequential matrix multiplication The two matrices are randomly
	 * generated
	 * 
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 */
	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
		double[][] resultMatrix = new double[MATRIX_SIZE][MATRIX_SIZE];
		for (int i = 0; i < MATRIX_SIZE; i++) {
			for (int j = 0; j < MATRIX_SIZE; j++) {
				resultMatrix[i][j] = 0;
				for (int k = 0; k < MATRIX_SIZE; k++) {
					resultMatrix[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return resultMatrix;
	}

	/**
	 * The printMatrixTest takes an input matrix and prints it on the terminal
	 * 
	 * @param matrixToPrint is the matrix to print
	 */
	public static void printMatrixTest(double[][] matrixToPrint) {
		for (int i = 0; i < MATRIX_SIZE; i++) {
			String curr_line = "";
			for (int j = 0; j < MATRIX_SIZE; j++) {
				curr_line = curr_line + (int) matrixToPrint[i][j];
				if (j == MATRIX_SIZE - 1) {
					System.out.println(curr_line);
				}
				curr_line = curr_line + " ";
			}
		}
	}

	/**
	 * Returns the result of a concurrent matrix multiplication The two matrices are randomly
	 * generated
	 * 
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 */
	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
		double[][] resultMatrix = new double[MATRIX_SIZE][MATRIX_SIZE];
		RecursiveAction mainTask = new MatrixMultiplyParallel(a, b, resultMatrix, 0, MATRIX_SIZE);
		ForkJoinPool pool = new ForkJoinPool(NUMBER_THREADS); // Create a ForkJoinPool with
																// NUMBER_THREADS processor(s)
		// ForkJoinPool pool = new ForkJoinPool(); // Create a ForkJoinPool with all
		// available processors
		pool.invoke(mainTask);

		return resultMatrix;

	}

	private static class MatrixMultiplyParallel extends RecursiveAction {
		private static final int THRESHOLD = 100;
		private double[][] a;
		private double[][] b;
		private double[][] resultMatrix;
		private int startRow;
		private int endRow;

		MatrixMultiplyParallel(double[][] a, double[][] b, double[][] resultMatrix, int startRow,
				int endRow) {
			this.a = a;
			this.b = b;
			this.resultMatrix = resultMatrix;
			this.startRow = startRow;
			this.endRow = endRow;
		}

		@Override
		protected void compute() {
			if (endRow - startRow <= THRESHOLD) {
				// Directly compute if the task is small enough
				for (int i = startRow; i < endRow; i++) {
					for (int j = 0; j < MATRIX_SIZE; j++) {
						resultMatrix[i][j] = 0;
						for (int k = 0; k < MATRIX_SIZE; k++) {
							resultMatrix[i][j] += a[i][k] * b[k][j];
						}
					}
				}
			} else {
				// Split the task into two subtasks
				int midRow = (startRow + endRow) / 2;
				MatrixMultiplyParallel subTask1 =
						new MatrixMultiplyParallel(a, b, resultMatrix, startRow, midRow);
				MatrixMultiplyParallel subTask2 =
						new MatrixMultiplyParallel(a, b, resultMatrix, midRow, endRow);
				invokeAll(subTask1, subTask2);
			}
		}
	}

	/**
	 * Populates a matrix of given size with randomly generated integers between 0-10.
	 * 
	 * @param numRows number of rows
	 * @param numCols number of cols
	 * @return matrix
	 */
	private static double[][] generateRandomMatrix(int numRows, int numCols) {
		double matrix[][] = new double[numRows][numCols];
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				matrix[row][col] = (double) ((int) (Math.random() * 10.0));
			}
		}
		return matrix;
	}

}
