import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

abstract class Employee {
    private String id;
    private String fullName;
    private String department;

    public Employee(String id, String fullName, String department) {
        setId(id);
        setFullName(fullName);
        setDepartment(department);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID must not be empty");
        }
        this.id = id.trim();
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department must not be empty");
        }
        this.department = department.trim();
    }

    public abstract double calculateSalary();

    public abstract String getType();

    public void displayInfo() {
        System.out.print("[" + getType() + "] ID=" + id + " | " + fullName + " | department="
                + department + " | salary=" + money(calculateSalary()) + " VND");
    }

    protected String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }
}

class FullTimeEmployee extends Employee {
    private double baseSalary;
    private double allowance;
    private int workdays;

    public FullTimeEmployee(String id, String fullName, String department,
            double baseSalary, double allowance, int workdays) {
        super(id, fullName, department);
        setBaseSalary(baseSalary);
        setAllowance(allowance);
        setWorkdays(workdays);
    }

    public FullTimeEmployee(String id, String fullName, String department, double baseSalary) {
        this(id, fullName, department, baseSalary, 0, 26);
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        if (baseSalary < 0) {
            throw new IllegalArgumentException("Base salary must not be negative");
        }
        this.baseSalary = baseSalary;
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        if (allowance < 0) {
            throw new IllegalArgumentException("Allowance must not be negative");
        }
        this.allowance = allowance;
    }

    public int getWorkdays() {
        return workdays;
    }

    public void setWorkdays(int workdays) {
        if (workdays < 0 || workdays > 31) {
            throw new IllegalArgumentException("Workdays must be between 0 and 31");
        }
        this.workdays = workdays;
    }

    @Override
    public double calculateSalary() {
        return baseSalary * Math.min(workdays, 26) / 26.0 + allowance;
    }

    @Override
    public String getType() {
        return "FullTime";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | base=" + money(baseSalary) + " | allowance=" + money(allowance)
                + " | workdays=" + workdays);
    }
}

class PartTimeEmployee extends Employee {
    private double hoursWorked;
    private double hourlyRate;

    public PartTimeEmployee(String id, String fullName, String department,
            double hoursWorked, double hourlyRate) {
        super(id, fullName, department);
        setHoursWorked(hoursWorked);
        setHourlyRate(hourlyRate);
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        if (hoursWorked < 0) {
            throw new IllegalArgumentException("Hours worked must not be negative");
        }
        this.hoursWorked = hoursWorked;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate must not be negative");
        }
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculateSalary() {
        return hoursWorked * hourlyRate;
    }

    @Override
    public String getType() {
        return "PartTime";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | hours=" + hoursWorked + " | rate=" + money(hourlyRate));
    }
}

public class Exercise2_EmployeePayrollManagement {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<Employee> EMPLOYEES = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    addFullTimeEmployee();
                    break;
                case 2:
                    addPartTimeEmployee();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    searchById();
                    break;
                case 5:
                    searchByNameAndDepartment();
                    break;
                case 6:
                    searchByDepartment();
                    break;
                case 7:
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
        System.out.println("=== Employee Payroll Management ===");
        System.out.println("1. Add full-time employee");
        System.out.println("2. Add part-time employee");
        System.out.println("3. Display all employees");
        System.out.println("4. Find employee by ID");
        System.out.println("5. Find employees by name and department");
        System.out.println("6. Find employees by department");
        System.out.println("7. Show statistics");
        System.out.println("0. Exit");
    }

    private static void addFullTimeEmployee() {
        String id = readNonEmpty("Employee ID: ");
        if (findEmployee(EMPLOYEES, id) != null) {
            System.out.println("Duplicate employee ID.");
            return;
        }
        String name = readNonEmpty("Full name: ");
        String department = readNonEmpty("Department: ");
        double baseSalary = readDoubleMin("Base salary: ", 0);
        double allowance = readDoubleMin("Allowance: ", 0);
        int workdays = readIntRange("Workdays: ", 0, 31);
        try {
            EMPLOYEES.add(new FullTimeEmployee(id, name, department, baseSalary, allowance, workdays));
            System.out.println("Full-time employee added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void addPartTimeEmployee() {
        String id = readNonEmpty("Employee ID: ");
        if (findEmployee(EMPLOYEES, id) != null) {
            System.out.println("Duplicate employee ID.");
            return;
        }
        String name = readNonEmpty("Full name: ");
        String department = readNonEmpty("Department: ");
        double hours = readDoubleMin("Hours worked: ", 0);
        double rate = readDoubleMin("Hourly rate: ", 0);
        try {
            EMPLOYEES.add(new PartTimeEmployee(id, name, department, hours, rate));
            System.out.println("Part-time employee added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void displayAll() {
        if (EMPLOYEES.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        for (Employee employee : EMPLOYEES) {
            employee.displayInfo();
        }
    }

    private static void searchById() {
        if (EMPLOYEES.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        String id = readNonEmpty("Employee ID to search: ");
        Employee employee = findEmployee(EMPLOYEES, id);
        if (employee == null) {
            System.out.println("No employee found.");
            return;
        }
        employee.displayInfo();
    }

    private static void searchByNameAndDepartment() {
        if (EMPLOYEES.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        String name = readNonEmpty("Full name: ");
        String department = readNonEmpty("Department: ");
        List<Employee> result = findEmployee(EMPLOYEES, name, department);
        if (result.isEmpty()) {
            System.out.println("No employee found.");
            return;
        }
        for (Employee employee : result) {
            employee.displayInfo();
        }
    }

    private static void searchByDepartment() {
        if (EMPLOYEES.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        String department = readNonEmpty("Department: ");
        boolean found = false;
        for (Employee employee : EMPLOYEES) {
            if (employee.getDepartment().equalsIgnoreCase(department)) {
                employee.displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No employee found.");
        }
    }

    private static void showStatistics() {
        if (EMPLOYEES.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        double totalPayroll = 0;
        int fullTime = 0;
        int partTime = 0;
        Employee highest = EMPLOYEES.get(0);
        for (Employee employee : EMPLOYEES) {
            double salary = employee.calculateSalary();
            totalPayroll += salary;
            if (employee instanceof FullTimeEmployee) {
                fullTime++;
            } else {
                partTime++;
            }
            if (salary > highest.calculateSalary()) {
                highest = employee;
            }
        }
        System.out.println("Full-time employees: " + fullTime);
        System.out.println("Part-time employees: " + partTime);
        System.out.println("Total payroll: " + money(totalPayroll) + " VND");
        System.out.println("Highest salary employee:");
        highest.displayInfo();
    }

    private static Employee findEmployee(List<Employee> employees, String id) {
        for (Employee employee : employees) {
            if (employee.getId().equalsIgnoreCase(id.trim())) {
                return employee;
            }
        }
        return null;
    }

    private static List<Employee> findEmployee(List<Employee> employees, String name, String department) {
        List<Employee> result = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getFullName().equalsIgnoreCase(name.trim())
                    && employee.getDepartment().equalsIgnoreCase(department.trim())) {
                result.add(employee);
            }
        }
        return result;
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

    private static int readIntRange(String label, int min, int max) {
        while (true) {
            int value = readInt(label);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Value must be between " + min + " and " + max + ".");
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
