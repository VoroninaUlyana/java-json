import java.util.List;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Введите имя первого файла: ");
        String file1 = sc.nextLine();

        System.out.print("Введите имя второго файла: ");
        String file2 = sc.nextLine();

        System.out.print("Введите имя результирующего файла: ");
        String resultFile = sc.nextLine();

        List<Student> students1 = Fileop.readStudents(file1);
        List<Student> students2 = Fileop.readStudents(file2);

        while (true) {
            System.out.println("\nВыберите операцию:");
            System.out.println("1 - Объединение");
            System.out.println("2 - Пересечение");
            System.out.println("3 - Разность (первый - второй)");
            System.out.println("0 - Выход");

            int choice = sc.nextInt();
            List<Student> result = null;

            switch (choice) {
                case 1:
                    result = Operations.union(students1, students2);
                    break;
                case 2:
                    result = Operations.intersection(students1, students2);
                    break;
                case 3:
                    result = Operations.difference(students1, students2);
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Неверный выбор");
            }

            if (result != null) {
                if (result.isEmpty()) {
                    try (PrintWriter pw = new PrintWriter(resultFile)) {
                        pw.println("Пустое множество");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Fileop.writeStudents(resultFile, result);
                }
                System.out.println("Результат записан в " + resultFile);
            }
        }
    }
}
