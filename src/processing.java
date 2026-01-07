import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class processing {
    private converter converter;
    private Scanner scanner;

    public processing(Scanner scanner) {
        this.converter = new converter();
        this.scanner = scanner;
    }

    public String[] processLine(String line) {
        List<Character> englishLetters = new ArrayList<>();
        List<Integer> letterNumbers = new ArrayList<>();

        for (char c : line.toCharArray()) {
            if (converter.isEnglishLetter(c)) {
                englishLetters.add(c);
                letterNumbers.add(converter.letterToNumber(c));
            }
        }

        if (englishLetters.isEmpty()) {
            return new String[]{line, "(нет английских букв)"};
        }

        StringBuilder lettersLine = new StringBuilder();
        StringBuilder numbersLine = new StringBuilder();

        for (int i = 0; i < englishLetters.size(); i++) {
            lettersLine.append(englishLetters.get(i));
            if (i < englishLetters.size() - 1) {
                lettersLine.append("  ");
            }
        }

        for (int i = 0; i < letterNumbers.size(); i++) {
            int currentNumber = letterNumbers.get(i);

            if (i == 0) {
                numbersLine.append(currentNumber);
            } else {
                int previousNumber = letterNumbers.get(i - 1);

                if (previousNumber < 10 && currentNumber < 10) {
                    numbersLine.append("  ").append(currentNumber);
                } else {
                    numbersLine.append(" ").append(currentNumber);
                }
            }
        }

        return new String[]{lettersLine.toString(), numbersLine.toString()};
    }

    public void processText() {
        input inputHandler = new input(scanner);

        List<String> textLines = inputHandler.readTextFromConsole();

        if (textLines.isEmpty()) {
            System.out.println("Текст не был введен.");
            return;
        }

        System.out.println("\nРезультат обработки:");
        System.out.println("====================");

        for (int i = 0; i < textLines.size(); i++) {
            String line = textLines.get(i);

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] result = processLine(line);

            System.out.println(result[0]);
            System.out.println(result[1]);
            System.out.println();
        }
    }

    public void demonstrate() {
        System.out.println("Демонстрация работы программы:");
        System.out.println("==============================");

        String[] demoLines = {
                "Hello World!",
                "Java Programming",
                "Test",
                "abc",
                "xyz"
        };

        for (String line : demoLines) {
            System.out.println("Исходная строка: " + line);
            String[] result = processLine(line);
            System.out.println(result[0]);
            System.out.println(result[1]);
            System.out.println();
        }
    }
}