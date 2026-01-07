import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Comparator;

class GradeBook
{
    private String lastName;
    private String firstName;
    private String middleName;
    private int course;
    private String group;
    private String specialty; //
    private List<Session> sessions = new ArrayList<>();

    public GradeBook(String lastName, String firstName, String middleName, int course, String group, String specialty)
    {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.course = course;
        this.group = group;
        this.specialty = specialty;
    }

    public class Session
    {
        private int number;
        private Map<String, Integer> exams = new LinkedHashMap<>();
        private Map<String, String> credits = new LinkedHashMap<>();

        public Session(int number)
        {
            this.number = number;
        }

        public void addExam(String subject, int grade)
        {
            exams.put(subject, grade);
        }

        public void addCredit(String subject, String status)
        {
            credits.put(subject, status);
        }

        public double getAverageExamGrade()
        {
            if (exams.isEmpty()) return 0;
            double sum = 0;
            for (int grade : exams.values()) sum += grade;
            return sum / exams.size();
        }

        public boolean allExamsExcellent()
        {
            for (int grade : exams.values())
                if (grade < 9) return false;
            return true;
        }

        public boolean allCreditsPassed()
        {
            for (String status : credits.values())
                if (!status.equalsIgnoreCase("сдан")) return false;
            return true;
        }

        public boolean hasDebts() {
            for (int grade : exams.values())
                if (grade < 4) return true;
            for (String status : credits.values())
                if (!status.equalsIgnoreCase("сдан")) return true;
            return false;
        }

        public int getNumber()
        {
            return number;
        }

        public Map<String, Integer> getExams()
        {
            return exams;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("  Сессия ").append(number).append(":\n");
            sb.append("    Экзамены:\n");
            for (var entry : exams.entrySet())
                sb.append("      ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            sb.append(String.format("    Средний балл: %.2f\n", getAverageExamGrade()));
            sb.append("    Зачеты:\n");
            for (var entry : credits.entrySet())
                sb.append("      ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            return sb.toString();
        }
    }

    public void addSession(Session session)
    {
        sessions.add(session);
    }

    public double getOverallAverage()
    {
        if (sessions.isEmpty()) return 0;
        double sum = 0;
        for (Session s : sessions) sum += s.getAverageExamGrade();
        return sum / sessions.size();
    }

    public boolean isExcellent()
    {
        for (Session s : sessions)
            if (!s.allExamsExcellent() || !s.allCreditsPassed()) return false;
        return true;
    }

    public boolean hasDebtsOverall()
    {
        for (Session s : sessions)
            if (s.hasDebts()) return true;
        return false;
    }

    public String getFullName()
    {
        return lastName + " " + firstName + " " + middleName;
    }

    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public List<Session> getSessions() { return sessions; }
    public int getCourse() { return course; }
    public String getGroup() { return group; }
    public String getSpecialty() { return specialty; } // ✅ геттер

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName())
                .append(", курс: ").append(course)
                .append(", группа: ").append(group)
                .append(", специальность: ").append(specialty).append("\n"); // ✅ добавлено
        for (Session s : sessions)
            sb.append(s.toString());
        sb.append("Отличник: ").append(isExcellent() ? "ДА" : "НЕТ").append("\n");
        sb.append(String.format("Общий средний балл: %.1f\n\n", getOverallAverage()));
        return sb.toString();
    }

    public static int getCourseRank(List<GradeBook> students, GradeBook target)
    {
        List<GradeBook> courseStudents = new ArrayList<>();
        for (GradeBook st : students)
            if (st.getCourse() == target.getCourse())
                courseStudents.add(st);
        courseStudents.sort((a, b) -> Double.compare(b.getOverallAverage(), a.getOverallAverage()));
        for (int i = 0; i < courseStudents.size(); i++)
            if (courseStudents.get(i) == target)
                return i + 1;
        return -1;
    }

    public static GradeBook findMinAverage(List<GradeBook> students)
    {
        return Collections.min(students, Comparator.comparingDouble(GradeBook::getOverallAverage));
    }

    public static GradeBook findMaxAverage(List<GradeBook> students)
    {
        return Collections.max(students, Comparator.comparingDouble(GradeBook::getOverallAverage));
    }
}

