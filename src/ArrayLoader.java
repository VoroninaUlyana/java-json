import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class ArrayLoader
{
    public static int[] load(String filename)
    {
        List<Integer> list = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filename)))
        {
            while (sc.hasNext())
            {
                if (sc.hasNextInt())
                {
                    list.add(sc.nextInt());
                } else
                {
                    sc.next();
                }
            }
        } catch (Exception e)
        {
            System.err.println("Ошибка чтения файла " + filename + ": " + e.getMessage());
        }
        return list.stream().mapToInt(i -> i).toArray();
    }
}
