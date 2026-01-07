package com.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MatrixFileReader {
    private final Scanner scanner;

    public MatrixFileReader() {
        this.scanner = new Scanner(System.in);
    }

    public void printTaskDescription() {
        System.out.println("""
            =====================================================
            Условие задачи:
            Отсортировать столбцы матрицы по возрастанию 
            минимальных элементов в столбцах.
            
            Матрица будет считана из файла.
            Формат файла: каждая строка файла - строка матрицы
            с числами, разделенными пробелами.
            =====================================================
            """);
    }

    public String getFileNameFromUser() {
        System.out.print("Введите имя файла с матрицей: ");
        return scanner.next();
    }

    public int[][] readMatrixFromFile(String filename) throws IOException {
        List<int[]> matrixList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] numbers = line.split("\\s+");
                int[] row = new int[numbers.length];

                for (int i = 0; i < numbers.length; i++) {
                    row[i] = Integer.parseInt(numbers[i]);
                }
                matrixList.add(row);
            }
        }

        // Проверяем, что все строки имеют одинаковую длину
        if (matrixList.size() > 0) {
            int cols = matrixList.get(0).length;
            for (int i = 1; i < matrixList.size(); i++) {
                if (matrixList.get(i).length != cols) {
                    throw new IOException("Все строки матрицы должны иметь одинаковое количество элементов");
                }
            }
        }
        int[][] matrix = new int[matrixList.size()][];
        for (int i = 0; i < matrixList.size(); i++) {
            matrix[i] = matrixList.get(i);
        }

        return matrix;
    }

    public void printMatrix(String title, int[][] matrix) {
        System.out.println(title);
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("Строка " + (i + 1) + ": ");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printError(String message) {
        System.out.println("Ошибка: " + message);
    }

    public void close() {
        scanner.close();
    }
}