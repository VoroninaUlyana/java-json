package com.matrix;

import java.io.IOException;

public class MatrixAnalyzer {
    private final MatrixFileReader fileReader;
    private final PermutationChecker checker;

    public MatrixAnalyzer() {
        this.fileReader = new MatrixFileReader();
        this.checker = new PermutationChecker();
    }

    public void analyzeMatrix() {
        try {
            fileReader.printTaskDescription();

            // Получаем имя файла от пользователя
            String filename = fileReader.getFileNameFromUser();

            int[][] matrix = fileReader.readMatrixFromFile(filename);

            fileReader.printMatrix("\nМатрица из файла:", matrix);

            int permutationCount = checker.countPermutationRows(matrix);

            fileReader.printResult(permutationCount);

        } catch (IOException e) {
            fileReader.printError("Ошибка чтения файла: " + e.getMessage());
        } catch (NumberFormatException e) {
            fileReader.printError("Неверный формат чисел в файле: " + e.getMessage());
        } catch (Exception e) {
            fileReader.printError("Произошла ошибка: " + e.getMessage());
        } finally {
            fileReader.close();
        }
    }
}