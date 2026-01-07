package com.matrix;

import java.io.IOException;

public class MatrixAnalyzer {
    private final MatrixOperations processor;
    private final MatrixFileReader fileReader;

    public MatrixAnalyzer() {
        this.processor = new MatrixOperations();
        this.fileReader = new MatrixFileReader();
    }

    public void analyzeMatrix() {
        try {
            fileReader.printTaskDescription();

            String filename = fileReader.getFileNameFromUser();

            int[][] matrix = fileReader.readMatrixFromFile(filename);

            int maxAbsoluteValue = fileReader.readInt("Введите максимальное абсолютное значение n: ");

            fileReader.printMatrix("\nИсходная матрица:", matrix);

            analyzeColumns(matrix, maxAbsoluteValue);

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

    private void analyzeColumns(int[][] matrix, int maxAbsoluteValue) {
        int targetColumn = -1;
        long minProduct = Long.MAX_VALUE;

        for (int j = 0; j < matrix[0].length; j++) {
            if (processor.isColumnValid(matrix, j, maxAbsoluteValue)) {
                long product = processor.calculateColumnProduct(matrix, j);
                System.out.printf("Столбец %d: произведение = %d%n", j, product);

                if (product < minProduct) {
                    minProduct = product;
                    targetColumn = j;
                }
            }
        }

        if (targetColumn != -1) {
            int[] columnElements = processor.getColumnElements(matrix, targetColumn);
            fileReader.printColumnInfo(targetColumn, minProduct, columnElements);
        } else {
            fileReader.printNoValidColumnsMessage(maxAbsoluteValue);
        }
    }
}