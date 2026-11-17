import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

abstract class LibraryItem {
    private String id;
    private String title;
    private String author;
    private int daysBorrowed;

    public LibraryItem(String id, String title, String author, int daysBorrowed) {
        setId(id);
        setTitle(title);
        setAuthor(author);
        setDaysBorrowed(daysBorrowed);
    }

    public LibraryItem(String id, String title, int daysBorrowed) {
        this(id, title, "Unknown", daysBorrowed);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID must not be empty");
        }
        this.id = id.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
        this.title = title.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author must not be empty");
        }
        this.author = author.trim();
    }

    public int getDaysBorrowed() {
        return daysBorrowed;
    }

    public void setDaysBorrowed(int daysBorrowed) {
        if (daysBorrowed < 1) {
            throw new IllegalArgumentException("Days borrowed must be at least 1");
        }
        this.daysBorrowed = daysBorrowed;
    }

    public abstract double calculateFee();

    public abstract String getType();

    public void displayInfo() {
        System.out.print("[" + getType() + "] ID=" + id + " | " + title + " | " + author
                + " | days=" + daysBorrowed);
    }

    protected String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }
}

class PrintedBook extends LibraryItem {
    private int pages;

    public PrintedBook(String id, String title, String author, int daysBorrowed, int pages) {
        super(id, title, author, daysBorrowed);
        setPages(pages);
    }

    public PrintedBook(String id, String title, int daysBorrowed, int pages) {
        this(id, title, "Unknown", daysBorrowed, pages);
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        if (pages < 1) {
            throw new IllegalArgumentException("Pages must be at least 1");
        }
        this.pages = pages;
    }

    @Override
    public double calculateFee() {
        return 5000.0 * getDaysBorrowed() + 10.0 * pages;
    }

    @Override
    public String getType() {
        return "PrintedBook";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | pages=" + pages + " | fee=" + money(calculateFee()) + " VND");
    }
}

class Magazine extends LibraryItem {
    private int issueNumber;

    public Magazine(String id, String title, String author, int daysBorrowed, int issueNumber) {
        super(id, title, author, daysBorrowed);
        setIssueNumber(issueNumber);
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        if (issueNumber < 1) {
            throw new IllegalArgumentException("Issue number must be at least 1");
        }
        this.issueNumber = issueNumber;
    }

    @Override
    public double calculateFee() {
        return 3000.0 * getDaysBorrowed() + 2000.0;
    }

    @Override
    public String getType() {
        return "Magazine";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | issue=" + issueNumber + " | fee=" + money(calculateFee()) + " VND");
    }
}

public class Exercise1_LibraryBorrowingFeeManagement {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<LibraryItem> ITEMS = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    addPrintedBook();
                    break;
                case 2:
                    addMagazine();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    searchById();
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
        System.out.println("=== Library Borrowing Fee Management ===");
        System.out.println("1. Add printed book");
        System.out.println("2. Add magazine");
        System.out.println("3. Display all items");
        System.out.println("4. Search by item ID");
        System.out.println("5. Show statistics");
        System.out.println("0. Exit");
    }

    private static void addPrintedBook() {
        String id = readNonEmpty("Item ID: ");
        if (findById(id) != null) {
            System.out.println("Duplicate item ID.");
            return;
        }
        String title = readNonEmpty("Title: ");
        String author = readNonEmpty("Author: ");
        int days = readIntMin("Days borrowed: ", 1);
        int pages = readIntMin("Pages: ", 1);
        try {
            ITEMS.add(new PrintedBook(id, title, author, days, pages));
            System.out.println("Printed book added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void addMagazine() {
        String id = readNonEmpty("Item ID: ");
        if (findById(id) != null) {
            System.out.println("Duplicate item ID.");
            return;
        }
        String title = readNonEmpty("Title: ");
        String author = readNonEmpty("Author: ");
        int days = readIntMin("Days borrowed: ", 1);
        int issue = readIntMin("Issue number: ", 1);
        try {
            ITEMS.add(new Magazine(id, title, author, days, issue));
            System.out.println("Magazine added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void displayAll() {
        if (ITEMS.isEmpty()) {
            System.out.println("No items.");
            return;
        }
        for (LibraryItem item : ITEMS) {
            item.displayInfo();
        }
    }

    private static void searchById() {
        if (ITEMS.isEmpty()) {
            System.out.println("No items.");
            return;
        }
        String id = readNonEmpty("Item ID to search: ");
        LibraryItem item = findById(id);
        if (item == null) {
            System.out.println("No item found.");
            return;
        }
        item.displayInfo();
    }

    private static void showStatistics() {
        if (ITEMS.isEmpty()) {
            System.out.println("No items.");
            return;
        }
        int printedBooks = 0;
        int magazines = 0;
        double totalRevenue = 0;
        LibraryItem highest = ITEMS.get(0);
        for (LibraryItem item : ITEMS) {
            if (item instanceof PrintedBook) {
                printedBooks++;
            } else {
                magazines++;
            }
            double fee = item.calculateFee();
            totalRevenue += fee;
            if (fee > highest.calculateFee()) {
                highest = item;
            }
        }
        System.out.println("Printed books: " + printedBooks);
        System.out.println("Magazines: " + magazines);
        System.out.println("Total revenue: " + money(totalRevenue) + " VND");
        System.out.println("Highest fee item:");
        highest.displayInfo();
    }

    private static LibraryItem findById(String id) {
        for (LibraryItem item : ITEMS) {
            if (item.getId().equalsIgnoreCase(id.trim())) {
                return item;
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
