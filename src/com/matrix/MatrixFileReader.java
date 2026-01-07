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
            Определить количество строк матрицы, которые являются 
            перестановками чисел от 1 до 20. Строка считается 
            перестановкой, если она содержит все числа от 1 до 20 
            ровно по одному разу в произвольном порядке.
            
            Матрица будет считана из файла.
            Формат файла: каждая строка файла - строка матрицы
            с 20 числами, разделенными пробелами.
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
                if (line.isEmpty()) continue; // Пропускаем пустые строки

                String[] numbers = line.split("\\s+");

                // Проверяем, что в строке ровно 20 чисел
                if (numbers.length != 20) {
                    throw new IOException("Строка должна содержать ровно 20 чисел: " + line);
                }

                int[] row = new int[20];
                for (int i = 0; i < 20; i++) {
                    row[i] = Integer.parseInt(numbers[i]);
                }
                matrixList.add(row);
            }
        }

        // Преобразуем список в двумерный массив
        int[][] matrix = new int[matrixList.size()][20];
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

    public void printResult(int permutationCount) {
        System.out.println("\nКоличество строк-перестановок: " + permutationCount);
    }

    public void printError(String message) {
        System.out.println("Ошибка: " + message);
    }

    public void close() {
        scanner.close();
    }
}