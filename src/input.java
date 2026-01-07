import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
public class input {
    private Scanner scanner;

    public input(Scanner scanner) {
        this.scanner = scanner;
    }
    public List<String> readTextFromConsole() {
        List<String> textLines = new ArrayList<>();

        System.out.println("Введите текст (пустая строка для завершения ввода):");
        System.out.println("==================================================");

        int lineNumber = 1;
        while (true) {
            System.out.printf("Строка %d: ", lineNumber);
            String line = scanner.nextLine();

            if (line.isEmpty()) {
                System.out.println("Завершение ввода...");
                break;
            }

            textLines.add(line);
            lineNumber++;
        }

        return textLines;
    }
    public boolean containsEnglishLetters(String line) {
        for (char c : line.toCharArray()) {
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                return true;
            }
        }
        return false;
    }
}