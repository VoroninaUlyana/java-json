package com.matrix;

public class PermutationChecker {
    private static final int REQUIRED_LENGTH = 20;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 20;

    public boolean isPermutation(int[] row) {
        if (row.length != REQUIRED_LENGTH) {
            return false;
        }

        boolean[] found = new boolean[MAX_VALUE + 1];

        for (int num : row) {
            if (num < MIN_VALUE || num > MAX_VALUE || found[num]) {
                return false;
            }
            found[num] = true;
        }

        return true;
    }

    public int countPermutationRows(int[][] matrix) {
        int count = 0;
        for (int[] row : matrix) {
            if (isPermutation(row)) {
                count++;
            }
        }
        return count;
    }
}
