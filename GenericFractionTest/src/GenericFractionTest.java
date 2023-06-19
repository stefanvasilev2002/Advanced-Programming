import java.util.Scanner;

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            new GenericFraction<>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}
class GenericFraction<T extends Number, U extends Number>{
    T numerator;
    U denominator;
    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        if(denominator.intValue() == 0){
            throw new ZeroDenominatorException();
        }
        this.denominator = denominator;
        this.numerator = numerator;
    }
    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        return new GenericFraction<>(numerator.doubleValue() * gf.denominator.doubleValue() + gf.numerator.doubleValue() * denominator.doubleValue(), denominator.doubleValue() * gf.denominator.doubleValue());
    }
    public double toDouble(){
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public String toString() {
        int gcd = findGCD(numerator.intValue(), denominator.intValue());
        return String.format("%.2f / %.2f", (double)numerator.intValue() / gcd, (double)denominator.intValue() / gcd);
    }
    public int findGCD(int num1, int num2) {
        if (num2 == 0) {
            return num1;
        }
        return findGCD(num2, num1 % num2);
    }
}
class ZeroDenominatorException extends Exception{
    public ZeroDenominatorException() {
        super("Denominator cannot be zero");
    }
}