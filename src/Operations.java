import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class Operations {

    public static List<Student> union(List<Student> list1, List<Student> list2) {
        Set<Student> set = new HashSet<>(list1);
        set.addAll(list2);
        return new ArrayList<>(set);
    }

    public static List<Student> intersection(List<Student> list1, List<Student> list2) {
        Set<Student> set1 = new HashSet<>(list1);
        set1.retainAll(list2);
        return new ArrayList<>(set1);
    }

    public static List<Student> difference(List<Student> list1, List<Student> list2) {
        Set<Student> set1 = new HashSet<>(list1);
        set1.removeAll(new HashSet<>(list2));
        return new ArrayList<>(set1);
    }
}
