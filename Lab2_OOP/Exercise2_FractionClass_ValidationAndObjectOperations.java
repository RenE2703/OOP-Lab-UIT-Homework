class Fraction {
    private int numerator;
    private int denominator;

    public Fraction() {
        this.numerator = 0;
        this.denominator = 1;
    }

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            this.numerator = numerator;
            this.denominator = 1;
        } else if (denominator < 0) {
            this.numerator = -numerator;
            this.denominator = -denominator;
        } else {
            this.numerator = numerator;
            this.denominator = denominator;
        }
    }

    public Fraction(Fraction other) {
        this.numerator = other.numerator;
        this.denominator = other.denominator;
    }

    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    public void simplify() {
        if (numerator == 0) {
            denominator = 1;
            return;
        }
        int g = gcd(numerator, denominator);
        numerator /= g;
        denominator /= g;
    }

    public Fraction add(Fraction other) {
        Fraction result = new Fraction(
                numerator * other.denominator + other.numerator * denominator,
                denominator * other.denominator);
        result.simplify();
        return result;
    }

    public Fraction subtract(Fraction other) {
        Fraction result = new Fraction(
                numerator * other.denominator - other.numerator * denominator,
                denominator * other.denominator);
        result.simplify();
        return result;
    }

    public Fraction multiply(Fraction other) {
        Fraction result = new Fraction(
                numerator * other.numerator,
                denominator * other.denominator);
        result.simplify();
        return result;
    }

    public Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("Cannot divide by a fraction equal to zero");
        }
        Fraction result = new Fraction(
                numerator * other.denominator,
                denominator * other.numerator);
        result.simplify();
        return result;
    }

    public void display() {
        System.out.println(numerator + "/" + denominator);
    }
}

public class Exercise2_FractionClass_ValidationAndObjectOperations {
    public static void main(String[] args) {
        Fraction zero = new Fraction();
        Fraction f1 = new Fraction(6, 8);
        Fraction f2 = new Fraction(1, -3);
        Fraction invalid = new Fraction(5, 0);

        System.out.print("Default fraction: ");
        zero.display();
        System.out.print("f1 = ");
        f1.display();
        System.out.print("f2 = ");
        f2.display();
        System.out.print("Fraction with denominator 0 -> ");
        invalid.display();

        Fraction copy = new Fraction(f1);
        System.out.print("Copy of f1: ");
        copy.display();
        System.out.println("copy == f1 (same reference)? " + (copy == f1));

        f1.simplify();
        System.out.print("f1 after simplify: ");
        f1.display();
        System.out.print("copy after f1 simplified (unchanged): ");
        copy.display();

        System.out.print("f1 + f2 = ");
        f1.add(f2).display();
        System.out.print("f1 - f2 = ");
        f1.subtract(f2).display();
        System.out.print("f1 * f2 = ");
        f1.multiply(f2).display();
        System.out.print("f1 / f2 = ");
        f1.divide(f2).display();
    }
}
