import java.io.*;
import java.util.*;
public class Main
{
    public static void main(String[] args) throws IOException
    {
        List<GradeBook> students = new ArrayList<>();
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        try (Scanner sc = new Scanner(inputFile, "UTF-8"))
        {
            GradeBook currentStudent = null;
            GradeBook.Session currentSession = null;
            while (sc.hasNextLine())
            {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("Студент:"))
                {
                    if (currentStudent != null && currentSession != null)
                        currentStudent.addSession(currentSession);
                    if (currentStudent != null)
                        students.add(currentStudent);
                    String[] parts = line.substring(9).trim().split(" ");
                    currentStudent = new GradeBook(parts[0], parts[1], parts[2],
                            Integer.parseInt(parts[3]), parts[4], parts[5]); // ✅ добавлено specialty
                    currentSession = null;
                } else if (line.startsWith("Сессия:"))
                {
                    if (currentSession != null)
                        currentStudent.addSession(currentSession);
                    currentSession = currentStudent.new Session(Integer.parseInt(line.split(":")[1].trim()));
                } else if (line.startsWith("Экзамен:"))
                {
                    String[] parts = line.substring(8).trim().split(" ");
                    String subject = parts[0];
                    int grade = Integer.parseInt(parts[1]);
                    currentSession.addExam(subject, grade);
                } else if (line.startsWith("Зачет:")) {
                    String[] parts = line.substring(6).trim().split(" ");
                    String subject = parts[0];
                    String status = parts[1];
                    currentSession.addCredit(subject, status);
                }
            }
            if (currentStudent != null)
            {
                if (currentSession != null)
                    currentStudent.addSession(currentSession);
                students.add(currentStudent);
            }
        }
        Scanner in = new Scanner(System.in, "UTF-8");
        try (PrintWriter out = new PrintWriter(outputFile, "UTF-8"))
        {
            out.println("ВСЕ СТУДЕНТЫ:");
            for (GradeBook st : students)
                out.print(st.toString());
            out.println("\nСТУДЕНТЫ-ОТЛИЧНИКИ:");
            List<GradeBook> excellent = new ArrayList<>();
            for (GradeBook st : students)
                if (st.isExcellent()) excellent.add(st);
            if (excellent.isEmpty()) out.println("Нет отличников!");
            else
            {
                for (GradeBook st : excellent)
                {
                    out.println("ФИО: " + st.getFullName());
                    out.println("Курс: " + st.getCourse());
                    out.println("Экзамены:");
                    for (GradeBook.Session s : st.getSessions())
                    {
                        out.println("  Сессия " + s.getNumber() + ":");
                        for (var e : s.getExams().entrySet())
                            out.println("      " + e.getKey() + ": " + e.getValue());
                        out.printf("  Средний балл за сессию: %.2f%n", s.getAverageExamGrade());
                    }
                    out.println();
                }
                out.println("Всего отличников: " + excellent.size());
            }
            students.sort((a, b) -> Double.compare(b.getOverallAverage(), a.getOverallAverage()));
            out.println("\nОТСОРТИРОВАННЫЙ СПИСОК СТУДЕНТОВ (по среднему баллу):");
            int i = 1;
            for (GradeBook st : students)
                out.printf("%d. %s — Средний балл: %.2f%n", i++, st.getFullName(), st.getOverallAverage());
            students.sort(Comparator.comparing(GradeBook::getLastName)
                    .thenComparing(GradeBook::getFirstName)
                    .thenComparing(GradeBook::getMiddleName));
            out.println("\nСТУДЕНТЫ ПО ФИО:");
            i = 1;
            for (GradeBook st : students)
                out.printf("%d. %s — Средний балл: %.2f%n", i++, st.getFullName(), st.getOverallAverage());
            students.sort(Comparator.comparingInt(GradeBook::getCourse));
            out.println("\nСТУДЕНТЫ ПО КУРСУ:");
            i = 1;
            for (GradeBook st : students)
                out.printf("%d. %s — Курс: %d, Средний балл: %.2f%n", i++, st.getFullName(), st.getCourse(), st.getOverallAverage());
            GradeBook minAvg = GradeBook.findMinAverage(students);
            GradeBook maxAvg = GradeBook.findMaxAverage(students);
            out.printf("\nСтудент с минимальным средним баллом: %s — %.2f%n", minAvg.getFullName(), minAvg.getOverallAverage());
            out.printf("Студент с максимальным средним баллом: %s — %.2f%n", maxAvg.getFullName(), maxAvg.getOverallAverage());
            out.println("\nСТУДЕНТЫ С ЗАДОЛЖЕННОСТЯМИ:");
            boolean hasDebts = false;
            for (GradeBook st : students)
            {
                if (st.hasDebtsOverall())
                {
                    out.println(st.getFullName() + " — средний балл: " + st.getOverallAverage());
                    hasDebts = true;
                }
            }
            if (!hasDebts) out.println("Нет студентов с задолженностями");
            System.out.println("\nЗАПРОС СРЕДНЕГО БАЛЛА ЗА СЕССИЮ:");
            System.out.print("Введите фамилию студента: ");
            String lastNameSession = in.nextLine().trim();
            GradeBook foundSession = findStudent(students, lastNameSession);
            if (foundSession == null)
            {
                System.out.println("Студент не найден!");
            } else {
                System.out.print("Введите номер сессии: ");
                int sessionNum = Integer.parseInt(in.nextLine().trim());
                if (sessionNum > 0 && sessionNum <= foundSession.getSessions().size())
                {
                    double avg = foundSession.getSessions().get(sessionNum - 1).getAverageExamGrade();
                    System.out.printf("%s, сессия %d — средний балл: %.2f%n", foundSession.getFullName(), sessionNum, avg);
                } else {
                    System.out.println("Сессия не найдена!");
                }
            }
            System.out.println("\nЗАПРОС ОБЩЕГО СРЕДНЕГО БАЛЛА:");
            System.out.print("Введите фамилию студента: ");
            String lastNameOverall = in.nextLine().trim();
            GradeBook foundOverall = findStudent(students, lastNameOverall);
            if (foundOverall == null)
            {
                System.out.println("Студент не найден!");
            } else {
                double avg = foundOverall.getOverallAverage();
                System.out.printf("%s — общий средний балл: %.2f%n", foundOverall.getFullName(), avg);
            }
            System.out.print("Введите фамилию студента для рейтинга по курсу: ");
            String lastNameRank = in.nextLine().trim();
            GradeBook foundRank = findStudent(students, lastNameRank);
            if (foundRank != null)
            {
                int rank = GradeBook.getCourseRank(students, foundRank);
                System.out.println(foundRank.getFullName() + " занимает " + rank + " место по курсу");
            } else
            {
                System.out.println("Студент не найден!");
            }
        }
        System.out.println("\n✅ Результаты записаны в файл output.txt");
    }
    private static GradeBook findStudent(List<GradeBook> list, String lastName)
    {
        for (GradeBook st : list)
            if (st.getLastName().equalsIgnoreCase(lastName))
                return st;
        return null;
    }
}
