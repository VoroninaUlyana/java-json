import java.util.Scanner;
public class Main {
    private static void displayMenu() {
        System.out.println("=== Конвертер текста в номера букв алфавита ===");
        System.out.println("1. Ввести текст с клавиатуры");
        System.out.println("2. Показать демонстрацию");
        System.out.println("3. Выход");
        System.out.print("Выберите опцию (1-3): ");
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        processing processor = new processing(scanner);

        boolean running = true;

        while (running) {
            displayMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println();
                    processor.processText();
                    break;

                case "2":
                    System.out.println();
                    processor.demonstrate();
                    break;

                case "3":
                    running = false;
                    System.out.println("Выход из программы...");
                    break;

                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    break;
            }

            if (running && !choice.equals("3")) {
                System.out.println("Нажмите Enter для продолжения...");
                scanner.nextLine();
            }
        }

        scanner.close();
        System.out.println("Спасибо за использование программы!");
    }
}