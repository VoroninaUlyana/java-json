package com.matrix;

import java.io.IOException;

public class MatrixAnalyzer {
    private final MatrixFileReader fileReader;
    private final MatrixSorter sorter;

    public MatrixAnalyzer() {
        this.fileReader = new MatrixFileReader();
        this.sorter = new MatrixSorter();
    }

    public void analyzeAndSortMatrix() {
        try {
            fileReader.printTaskDescription();

            String filename = fileReader.getFileNameFromUser();

            int[][] matrix = fileReader.readMatrixFromFile(filename);

            fileReader.printMatrix("\nИсходная матрица:", matrix);

            sorter.sortColumnsByMinValue(matrix);

            // Выводим отсортированную матрицу
            fileReader.printMatrix("\nМатрица после сортировки столбцов по минимальным элементам:", matrix);

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