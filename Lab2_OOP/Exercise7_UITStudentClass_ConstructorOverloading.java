class UITStudent {
    private String studentId;
    private String fullName;
    private String major;
    private double gpa;

    public UITStudent() {
        this("N/A", "Unknown");
    }

    public UITStudent(String studentId, String fullName) {
        this(studentId, fullName, "Undeclared", 0.0);
    }

    public UITStudent(String studentId, String fullName, String major, double gpa) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.major = major;
        this.gpa = (gpa >= 0.0 && gpa <= 10.0) ? gpa : 0.0;
    }

    public void displayInfo() {
        System.out.println(studentId + " - " + fullName + " - " + major
                + " - GPA: " + gpa + " - Passed: " + isPassed()
                + " - Classification: " + academicClassification());
    }

    public boolean isPassed() {
        return gpa >= 5.0;
    }

    public String academicClassification() {
        if (gpa >= 9.0) {
            return "Excellent";
        } else if (gpa >= 8.0) {
            return "Very Good";
        } else if (gpa >= 7.0) {
            return "Good";
        } else if (gpa >= 5.0) {
            return "Average";
        } else {
            return "Weak";
        }
    }
}

public class Exercise7_UITStudentClass_ConstructorOverloading {
    public static void main(String[] args) {
        UITStudent s1 = new UITStudent();
        UITStudent s2 = new UITStudent("23520001", "Nguyen Van An");
        UITStudent s3 = new UITStudent("23520002", "Tran Thi Binh", "Software Engineering", 9.2);
        UITStudent s4 = new UITStudent("23520003", "Le Van Cuong", "Computer Science", 7.4);
        UITStudent s5 = new UITStudent("23520004", "Pham Thi Dung", "Information Systems", 4.3);
        UITStudent s6 = new UITStudent("23520005", "Hoang Van Em", "Computer Engineering", 15.0);

        s1.displayInfo();
        s2.displayInfo();
        s3.displayInfo();
        s4.displayInfo();
        s5.displayInfo();
        s6.displayInfo();
    }
}
