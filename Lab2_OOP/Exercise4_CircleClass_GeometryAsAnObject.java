class Circle {
    private double centerX;
    private double centerY;
    private double radius;

    public Circle() {
        this.centerX = 0;
        this.centerY = 0;
        this.radius = 1;
    }

    public Circle(double centerX, double centerY, double radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius > 0 ? radius : 1;
    }

    public double area() {
        return Math.PI * radius * radius;
    }

    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    public boolean contains(double x, double y) {
        double dx = x - centerX;
        double dy = y - centerY;
        return dx * dx + dy * dy <= radius * radius;
    }

    public void displayInfo() {
        System.out.printf("Circle C(%.1f, %.1f, %.1f) | area=%.4f | perimeter=%.4f%n",
                centerX, centerY, radius, area(), perimeter());
    }
}

public class Exercise4_CircleClass_GeometryAsAnObject {
    public static void main(String[] args) {
        Circle c1 = new Circle();
        Circle c2 = new Circle(2, 3, 5);
        Circle c3 = new Circle(-1, 4, -2);

        c1.displayInfo();
        c2.displayInfo();
        c3.displayInfo();

        double[][] points = {{0, 0}, {1, 0}, {0.8, 0.8}, {2, 3}, {7, 3}, {7.1, 3}, {-1, 5}};

        for (double[] p : points) {
            System.out.println("Point (" + p[0] + ", " + p[1] + ") in c1: " + c1.contains(p[0], p[1]));
            System.out.println("Point (" + p[0] + ", " + p[1] + ") in c2: " + c2.contains(p[0], p[1]));
            System.out.println("Point (" + p[0] + ", " + p[1] + ") in c3: " + c3.contains(p[0], p[1]));
        }
    }
}
