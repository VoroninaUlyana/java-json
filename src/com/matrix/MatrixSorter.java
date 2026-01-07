package com.matrix;

import java.util.Arrays;
import java.util.Comparator;

public class MatrixSorter {

    public void sortColumnsByMinValue(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return;
        }

        int cols = matrix[0].length;
        Integer[] columnIndices = new Integer[cols];

        for (int j = 0; j < cols; j++) {
            columnIndices[j] = j;
        }

        Arrays.sort(columnIndices, new ColumnMinComparator(matrix));

        int[][] sortedMatrix = createSortedMatrix(matrix, columnIndices);

        copyMatrix(sortedMatrix, matrix);
    }

    private int[][] createSortedMatrix(int[][] matrix, Integer[] columnIndices) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] sortedMatrix = new int[rows][cols];

        for (int j = 0; j < cols; j++) {
            int originalCol = columnIndices[j];
            for (int i = 0; i < rows; i++) {
                sortedMatrix[i][j] = matrix[i][originalCol];
            }
        }

        return sortedMatrix;
    }

    private void copyMatrix(int[][] source, int[][] destination) {
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, source[0].length);
        }
    }

    public int findColumnMin(int[][] matrix, int colIndex) {
        int min = matrix[0][colIndex];
        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i][colIndex] < min) {
                min = matrix[i][colIndex];
            }
        }
        return min;
    }
    private class ColumnMinComparator implements Comparator<Integer> {
        private final int[][] matrix;

        public ColumnMinComparator(int[][] matrix) {
            this.matrix = matrix;
        }

        @Override
        public int compare(Integer col1, Integer col2) {
            int min1 = findColumnMin(matrix, col1);
            int min2 = findColumnMin(matrix, col2);
            return Integer.compare(min1, min2);
        }
    }
}
