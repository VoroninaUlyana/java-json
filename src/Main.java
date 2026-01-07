import com.company.exponential.ExponentialCalculator;
import com.company.exponential.ResultFormatter;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useLocale(Locale.US);
        System.out.print("Введите x: ");
        BigDecimal x = sc.nextBigDecimal();
        System.out.print("Введите k: ");
        int k = sc.nextInt();
        ExponentialCalculator calculator = new ExponentialCalculator(k);
        ResultFormatter formatter = new ResultFormatter();
        BigDecimal approximateValue = calculator.calculateExponential(x);
        double realValueDouble = Math.exp(x.doubleValue());
        BigDecimal realValue = BigDecimal.valueOf(realValueDouble);
        formatter.printResults(approximateValue, realValue,
                calculator.getEpsilon(), calculator.getIterations());
        sc.close();
    }
}





