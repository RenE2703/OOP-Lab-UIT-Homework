class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public double add(double a, double b) {
        return a + b;
    }

    public int add(int a, int b, int c) {
        return a + b + c;
    }

    public int max(int a, int b) {
        return a > b ? a : b;
    }

    public double max(double a, double b) {
        return a > b ? a : b;
    }
}

public class Exercise6_CalculatorClass_MethodOverloading {
    public static void main(String[] args) {
        Calculator calc = new Calculator();

        System.out.println("add(2, 3) = " + calc.add(2, 3));
        System.out.println("  -> selects add(int, int): both arguments are int");

        System.out.println("add(2.5, 3.1) = " + calc.add(2.5, 3.1));
        System.out.println("  -> selects add(double, double): both arguments are double");

        System.out.println("add(1, 2, 3) = " + calc.add(1, 2, 3));
        System.out.println("  -> selects add(int, int, int): three arguments");

        System.out.println("add(2, 3.5) = " + calc.add(2, 3.5));
        System.out.println("  -> selects add(double, double): int 2 is widened to double");

        System.out.println("max(7, 9) = " + calc.max(7, 9));
        System.out.println("  -> selects max(int, int): both arguments are int");

        System.out.println("max(7.5, 3.2) = " + calc.max(7.5, 3.2));
        System.out.println("  -> selects max(double, double): both arguments are double");

        System.out.println("max(4, 4.5) = " + calc.max(4, 4.5));
        System.out.println("  -> selects max(double, double): int 4 is widened to double");
    }
}
