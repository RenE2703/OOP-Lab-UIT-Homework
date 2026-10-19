class Rectangle {
    private double width;
    private double height;

    public Rectangle() {
        this.width = 1;
        this.height = 1;
    }

    public Rectangle(double width, double height) {
        this.width = width > 0 ? width : 1;
        this.height = height > 0 ? height : 1;
    }

    public double area() {
        return width * height;
    }

    public double perimeter() {
        return 2 * (width + height);
    }

    public void displayInfo() {
        System.out.println("Rectangle [width=" + width + ", height=" + height
                + ", area=" + area() + ", perimeter=" + perimeter() + "]");
    }
}

public class Exercise1_RectangleClass_BasicClassDesign {
    public static void main(String[] args) {
        Rectangle r1 = new Rectangle();
        Rectangle r2 = new Rectangle(4.5, 2);
        Rectangle r3 = new Rectangle(-3, 0);

        r1.displayInfo();
        r2.displayInfo();
        r3.displayInfo();

        System.out.println("Area of r2: " + r2.area());
        System.out.println("Perimeter of r2: " + r2.perimeter());
    }
}
