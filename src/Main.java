import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ширину строки: ");
        int width = scanner.nextInt();
        scanner.close();

        File fileManager = new File("input.txt", "output.txt");
        // строки с сохранением отступов
        List<Line> lines = fileManager.readFile();

        Formatter formatter = new Formatter(width);
        List<String> formattedLines = formatter.formatText(lines);

        fileManager.writeFile(formattedLines);

        System.out.println("Текст выровнен и записан в output.txt");
    }
}




