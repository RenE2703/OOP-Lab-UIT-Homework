import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

abstract class Student {
    private String studentId;
    private String fullName;
    private String address;
    private int totalCredits;
    private double gpa;

    public Student(String studentId, String fullName, String address, int totalCredits, double gpa) {
        setStudentId(studentId);
        setFullName(fullName);
        setAddress(address);
        setTotalCredits(totalCredits);
        setGpa(gpa);
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID must not be empty");
        }
        this.studentId = studentId.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name must not be empty");
        }
        this.fullName = fullName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address must not be empty");
        }
        this.address = address.trim();
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        if (totalCredits < 0) {
            throw new IllegalArgumentException("Total credits must not be negative");
        }
        this.totalCredits = totalCredits;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        if (gpa < 0 || gpa > 10) {
            throw new IllegalArgumentException("GPA must be between 0 and 10");
        }
        this.gpa = gpa;
    }

    public abstract boolean isEligible();

    public abstract String getProgram();

    public void displayInfo() {
        System.out.print("[" + getProgram() + "] ID=" + studentId + " | " + fullName
                + " | address=" + address + " | credits=" + totalCredits
                + " | GPA=" + String.format(Locale.US, "%.2f", gpa)
                + " | eligible=" + isEligible());
    }
}

class CollegeStudent extends Student {
    private double graduationExamScore;

    public CollegeStudent(String studentId, String fullName, String address,
            int totalCredits, double gpa, double graduationExamScore) {
        super(studentId, fullName, address, totalCredits, gpa);
        setGraduationExamScore(graduationExamScore);
    }

    public double getGraduationExamScore() {
        return graduationExamScore;
    }

    public void setGraduationExamScore(double graduationExamScore) {
        if (graduationExamScore < 0 || graduationExamScore > 10) {
            throw new IllegalArgumentException("Graduation exam score must be between 0 and 10");
        }
        this.graduationExamScore = graduationExamScore;
    }

    @Override
    public boolean isEligible() {
        return getTotalCredits() >= 120
                && getGpa() >= 5.0
                && graduationExamScore >= 5.0;
    }

    @Override
    public String getProgram() {
        return "College";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | exam=" + String.format(Locale.US, "%.2f", graduationExamScore));
    }
}

class UniversityStudent extends Student {
    private String thesisTitle;
    private double thesisScore;

    public UniversityStudent(String studentId, String fullName, String address,
            int totalCredits, double gpa, String thesisTitle, double thesisScore) {
        super(studentId, fullName, address, totalCredits, gpa);
        setThesisTitle(thesisTitle);
        setThesisScore(thesisScore);
    }

    public String getThesisTitle() {
        return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
        if (thesisTitle == null || thesisTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Thesis title must not be empty");
        }
        this.thesisTitle = thesisTitle.trim();
    }

    public double getThesisScore() {
        return thesisScore;
    }

    public void setThesisScore(double thesisScore) {
        if (thesisScore < 0 || thesisScore > 10) {
            throw new IllegalArgumentException("Thesis score must be between 0 and 10");
        }
        this.thesisScore = thesisScore;
    }

    @Override
    public boolean isEligible() {
        return getTotalCredits() >= 170
                && getGpa() >= 5.0
                && thesisScore >= 5.0;
    }

    @Override
    public String getProgram() {
        return "University";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | thesis=" + thesisTitle
                + " | thesis score=" + String.format(Locale.US, "%.2f", thesisScore));
    }
}

public class Exercise1_UniversityStudentGraduationManagement {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<Student> STUDENTS = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    addCollegeStudent();
                    break;
                case 2:
                    addUniversityStudent();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    countEligible();
                    break;
                case 5:
                    findTopUniversityStudent();
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
        System.out.println("=== University Student Graduation Management ===");
        System.out.println("1. Add college student");
        System.out.println("2. Add university student");
        System.out.println("3. Display all students");
        System.out.println("4. Count eligible students");
        System.out.println("5. Find university student with highest GPA");
        System.out.println("0. Exit");
    }

    private static void addCollegeStudent() {
        String id = readNonEmpty("Student ID: ");
        if (findById(id) != null) {
            System.out.println("Duplicate student ID.");
            return;
        }
        String name = readNonEmpty("Full name: ");
        String address = readNonEmpty("Address: ");
        int credits = readIntMin("Total credits: ", 0);
        double gpa = readDoubleRange("GPA: ", 0, 10);
        double examScore = readDoubleRange("Graduation exam score: ", 0, 10);
        try {
            STUDENTS.add(new CollegeStudent(id, name, address, credits, gpa, examScore));
            System.out.println("College student added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void addUniversityStudent() {
        String id = readNonEmpty("Student ID: ");
        if (findById(id) != null) {
            System.out.println("Duplicate student ID.");
            return;
        }
        String name = readNonEmpty("Full name: ");
        String address = readNonEmpty("Address: ");
        int credits = readIntMin("Total credits: ", 0);
        double gpa = readDoubleRange("GPA: ", 0, 10);
        String thesisTitle = readNonEmpty("Thesis title: ");
        double thesisScore = readDoubleRange("Thesis score: ", 0, 10);
        try {
            STUDENTS.add(new UniversityStudent(id, name, address, credits, gpa,
                    thesisTitle, thesisScore));
            System.out.println("University student added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void displayAll() {
        if (STUDENTS.isEmpty()) {
            System.out.println("No students.");
            return;
        }
        for (Student student : STUDENTS) {
            student.displayInfo();
        }
    }

    private static void countEligible() {
        if (STUDENTS.isEmpty()) {
            System.out.println("No students.");
            return;
        }
        int eligible = 0;
        for (Student student : STUDENTS) {
            if (student.isEligible()) {
                eligible++;
            }
        }
        System.out.println("Eligible students: " + eligible + "/" + STUDENTS.size());
    }

    private static void findTopUniversityStudent() {
        UniversityStudent best = null;
        for (Student student : STUDENTS) {
            if (student instanceof UniversityStudent) {
                UniversityStudent current = (UniversityStudent) student;
                if (best == null || current.getGpa() > best.getGpa()) {
                    best = current;
                }
            }
        }
        if (best == null) {
            System.out.println("No university student found.");
            return;
        }
        System.out.println("University student with highest GPA:");
        best.displayInfo();
    }

    private static Student findById(String id) {
        for (Student student : STUDENTS) {
            if (student.getStudentId().equalsIgnoreCase(id.trim())) {
                return student;
            }
        }
        return null;
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

    private static double readDoubleRange(String label, double min, double max) {
        while (true) {
            System.out.print(label);
            String line = SCANNER.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Value must be between " + min + " and " + max + ".");
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
