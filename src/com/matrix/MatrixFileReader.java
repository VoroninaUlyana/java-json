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
            Среди столбцов заданной матрицы, содержащих только
            такие элементы, которые по модулю не больше n, 
            найти столбец с минимальным произведением элементов.
            
            Матрица будет считана из файла.
            Формат файла: первая строка - размеры матрицы,
            последующие строки - элементы матрицы построчно.
            =====================================================
            """);
    }

    public int readInt(String prompt) {
        System.out.print(prompt);
        return scanner.nextInt();
    }

    public int[][] readMatrixFromFile(String filename) throws IOException {
        List<int[]> matrixList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.trim().split("\\s+");
                int[] row = new int[numbers.length];
                for (int i = 0; i < numbers.length; i++) {
                    row[i] = Integer.parseInt(numbers[i]);
                }
                matrixList.add(row);
            }
        }

        int[][] matrix = new int[matrixList.size()][];
        for (int i = 0; i < matrixList.size(); i++) {
            matrix[i] = matrixList.get(i);
        }

        return matrix;
    }

    public String getFileNameFromUser() {
        System.out.print("Введите имя файла с матрицей: ");
        return scanner.next();
    }

    public void printMatrix(String title, int[][] matrix) {
        System.out.println(title);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%4d", matrix[i][j]);
            }
            System.out.println();
        }
    }

    public void printColumnInfo(int columnIndex, long product, int[] columnElements) {
        System.out.printf("\nСтолбец с минимальным произведением: %d%n", columnIndex);
        System.out.printf("Произведение элементов: %d%n", product);
        System.out.println("Элементы столбца:");
        for (int element : columnElements) {
            System.out.println(element);
        }
    }

    public void printNoValidColumnsMessage(int n) {
        System.out.printf("\nНет столбцов, удовлетворяющих условию (все элементы по модулю ≤ %d)%n", n);
    }

    public void printError(String message) {
        System.out.println("Ошибка: " + message);
    }

    public void close() {
        scanner.close();
    }
}