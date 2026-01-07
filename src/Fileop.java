import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;

public class Fileop {

    public static List<Student> readStudents(String fileName) {
        List<Student> students = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(fileName))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                if (parts.length != 4) continue;
                long num = Long.parseLong(parts[0]);
                String name = parts[1];
                int group = Integer.parseInt(parts[2]);
                double grade = Double.parseDouble(parts[3]);
                students.add(new Student(num, name, group, grade));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + fileName);
        }
        return students;
    }

    public static void writeStudents(String fileName, List<Student> students) {
        try (PrintWriter pw = new PrintWriter(new File(fileName))) {
            for (Student s : students) {
                pw.println(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
