package com.matrix;

public class MatrixOperations {
    public boolean isColumnValid(int[][] matrix, int columnIndex, int maxAbsoluteValue) {
        for (int i = 0; i < matrix.length; i++) {
            if (Math.abs(matrix[i][columnIndex]) > maxAbsoluteValue) {
                return false;
            }
        }
        return true;
    }

    public long calculateColumnProduct(int[][] matrix, int columnIndex) {
        long product = 1;
        for (int i = 0; i < matrix.length; i++) {
            product *= matrix[i][columnIndex];
        }
        return product;
    }

    public int[] getColumnElements(int[][] matrix, int columnIndex) {
        int[] column = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            column[i] = matrix[i][columnIndex];
        }
        return column;
    }
}