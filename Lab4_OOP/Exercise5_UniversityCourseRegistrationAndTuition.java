import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

abstract class CourseRegistration {
    private String courseCode;
    private String courseName;
    private int credits;
    private int registeredStudents;

    public CourseRegistration(String courseCode, String courseName, int credits, int registeredStudents) {
        setCourseCode(courseCode);
        setCourseName(courseName);
        setCredits(credits);
        setRegisteredStudents(registeredStudents);
    }

    public CourseRegistration(String courseCode, String courseName, int credits) {
        this(courseCode, courseName, credits, 0);
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code must not be empty");
        }
        this.courseCode = courseCode.trim();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name must not be empty");
        }
        this.courseName = courseName.trim();
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        if (credits < 1) {
            throw new IllegalArgumentException("Credits must be at least 1");
        }
        this.credits = credits;
    }

    public int getRegisteredStudents() {
        return registeredStudents;
    }

    public void setRegisteredStudents(int registeredStudents) {
        if (registeredStudents < 0) {
            throw new IllegalArgumentException("Registered students must not be negative");
        }
        this.registeredStudents = registeredStudents;
    }

    public abstract double tuitionPerStudent();

    public abstract String getType();

    public double getExpectedTotal() {
        return tuitionPerStudent() * registeredStudents;
    }

    public void displayInfo() {
        System.out.print("[" + getType() + "] code=" + courseCode + " | " + courseName
                + " | credits=" + credits + " | students=" + registeredStudents
                + " | tuition/student=" + money(tuitionPerStudent())
                + " VND | expected total=" + money(getExpectedTotal()) + " VND");
    }

    protected String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }
}

class TheoryCourse extends CourseRegistration {
    public TheoryCourse(String courseCode, String courseName, int credits, int registeredStudents) {
        super(courseCode, courseName, credits, registeredStudents);
    }

    public TheoryCourse(String courseCode, String courseName, int credits) {
        super(courseCode, courseName, credits);
    }

    @Override
    public double tuitionPerStudent() {
        return getCredits() * 450000.0;
    }

    @Override
    public String getType() {
        return "Theory";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println();
    }
}

class LabCourse extends CourseRegistration {
    private double equipmentFee;

    public LabCourse(String courseCode, String courseName, int credits,
            int registeredStudents, double equipmentFee) {
        super(courseCode, courseName, credits, registeredStudents);
        setEquipmentFee(equipmentFee);
    }

    public double getEquipmentFee() {
        return equipmentFee;
    }

    public void setEquipmentFee(double equipmentFee) {
        if (equipmentFee < 0) {
            throw new IllegalArgumentException("Equipment fee must not be negative");
        }
        this.equipmentFee = equipmentFee;
    }

    @Override
    public double tuitionPerStudent() {
        return getCredits() * 600000.0 + equipmentFee;
    }

    @Override
    public String getType() {
        return "Laboratory";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | equipment fee=" + money(equipmentFee) + " VND");
    }
}

public class Exercise5_UniversityCourseRegistrationAndTuition {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<CourseRegistration> COURSES = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    addTheoryCourse();
                    break;
                case 2:
                    addLabCourse();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    searchByCode();
                    break;
                case 5:
                    showStatistics();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
            System.out.println();
        }
        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("=== University Course Registration and Tuition ===");
        System.out.println("1. Add theory course");
        System.out.println("2. Add laboratory course");
        System.out.println("3. Display all courses");
        System.out.println("4. Search by course code");
        System.out.println("5. Show statistics");
        System.out.println("0. Exit");
    }

    private static void addTheoryCourse() {
        String code = readNonEmpty("Course code: ");
        if (findByCode(code) != null) {
            System.out.println("Duplicate course code.");
            return;
        }
        String name = readNonEmpty("Course name: ");
        int credits = readIntMin("Credits: ", 1);
        int students = readIntMin("Registered students: ", 0);
        try {
            COURSES.add(new TheoryCourse(code, name, credits, students));
            System.out.println("Theory course added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void addLabCourse() {
        String code = readNonEmpty("Course code: ");
        if (findByCode(code) != null) {
            System.out.println("Duplicate course code.");
            return;
        }
        String name = readNonEmpty("Course name: ");
        int credits = readIntMin("Credits: ", 1);
        int students = readIntMin("Registered students: ", 0);
        double equipmentFee = readDoubleMin("Equipment fee per student: ", 0);
        try {
            COURSES.add(new LabCourse(code, name, credits, students, equipmentFee));
            System.out.println("Laboratory course added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void displayAll() {
        if (COURSES.isEmpty()) {
            System.out.println("No courses.");
            return;
        }
        for (CourseRegistration course : COURSES) {
            course.displayInfo();
        }
    }

    private static void searchByCode() {
        if (COURSES.isEmpty()) {
            System.out.println("No courses.");
            return;
        }
        String code = readNonEmpty("Course code to search: ");
        CourseRegistration course = findByCode(code);
        if (course == null) {
            System.out.println("No course found.");
            return;
        }
        course.displayInfo();
    }

    private static void showStatistics() {
        if (COURSES.isEmpty()) {
            System.out.println("No courses.");
            return;
        }
        int theory = 0;
        int laboratory = 0;
        int totalStudents = 0;
        double totalExpected = 0;
        CourseRegistration largest = COURSES.get(0);
        for (CourseRegistration course : COURSES) {
            if (course instanceof TheoryCourse) {
                theory++;
            } else {
                laboratory++;
            }
            totalStudents += course.getRegisteredStudents();
            totalExpected += course.getExpectedTotal();
            if (course.getExpectedTotal() > largest.getExpectedTotal()) {
                largest = course;
            }
        }
        System.out.println("Theory courses: " + theory);
        System.out.println("Laboratory courses: " + laboratory);
        System.out.println("Total expected collection: " + money(totalExpected) + " VND");
        System.out.println("Largest expected collection:");
        largest.displayInfo();
        if (totalStudents == 0) {
            System.out.println("Weighted average tuition per student: N/A");
        } else {
            System.out.printf(Locale.US, "Weighted average tuition per student: %,.2f VND%n",
                    totalExpected / totalStudents);
        }
    }

    private static CourseRegistration findByCode(String code) {
        for (CourseRegistration course : COURSES) {
            if (course.getCourseCode().equalsIgnoreCase(code.trim())) {
                return course;
            }
        }
        return null;
    }

    private static String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }

    private static int readInt(String label) {
        while (true) {
            System.out.print(label);
            String line = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer.");
            }
        }
    }

    private static int readIntMin(String label, int min) {
        while (true) {
            int value = readInt(label);
            if (value >= min) {
                return value;
            }
            System.out.println("Value must be at least " + min + ".");
        }
    }

    private static double readDoubleMin(String label, double min) {
        while (true) {
            System.out.print(label);
            String line = SCANNER.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value >= min) {
                    return value;
                }
                System.out.println("Value must be at least " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    private static String readNonEmpty(String label) {
        while (true) {
            System.out.print(label);
            String line = SCANNER.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Value must not be empty.");
        }
    }
}
