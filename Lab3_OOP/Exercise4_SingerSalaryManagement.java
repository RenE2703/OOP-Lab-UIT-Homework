import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

abstract class Singer {
    private String fullName;
    private int yearsExperience;
    private int albumsSold;
    private int performances;

    public Singer(String fullName, int yearsExperience, int albumsSold, int performances) {
        setFullName(fullName);
        setYearsExperience(yearsExperience);
        setAlbumsSold(albumsSold);
        setPerformances(performances);
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

    public int getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(int yearsExperience) {
        if (yearsExperience < 0) {
            throw new IllegalArgumentException("Years of experience must not be negative");
        }
        this.yearsExperience = yearsExperience;
    }

    public int getAlbumsSold() {
        return albumsSold;
    }

    public void setAlbumsSold(int albumsSold) {
        if (albumsSold < 0) {
            throw new IllegalArgumentException("Albums sold must not be negative");
        }
        this.albumsSold = albumsSold;
    }

    public int getPerformances() {
        return performances;
    }

    public void setPerformances(int performances) {
        if (performances < 0) {
            throw new IllegalArgumentException("Performances must not be negative");
        }
        this.performances = performances;
    }

    public abstract double calculateSalary();

    public abstract String getType();

    public void displayInfo() {
        System.out.print("[" + getType() + "] " + fullName + " | years=" + yearsExperience
                + " | albums=" + albumsSold + " | performances=" + performances
                + " | salary=" + money(calculateSalary()) + " VND");
    }

    protected String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }
}

class RegularSinger extends Singer {
    public RegularSinger(String fullName, int yearsExperience, int albumsSold, int performances) {
        super(fullName, yearsExperience, albumsSold, performances);
    }

    @Override
    public double calculateSalary() {
        return 3000000.0
                + 500000.0 * getYearsExperience()
                + 1000.0 * getAlbumsSold()
                + 200000.0 * getPerformances();
    }

    @Override
    public String getType() {
        return "Regular";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println();
    }
}

class FamousSinger extends Singer {
    private int gameShows;

    public FamousSinger(String fullName, int yearsExperience, int albumsSold,
            int performances, int gameShows) {
        super(fullName, yearsExperience, albumsSold, performances);
        setGameShows(gameShows);
    }

    public int getGameShows() {
        return gameShows;
    }

    public void setGameShows(int gameShows) {
        if (gameShows < 0) {
            throw new IllegalArgumentException("Game shows must not be negative");
        }
        this.gameShows = gameShows;
    }

    @Override
    public double calculateSalary() {
        return 5000000.0
                + 500000.0 * getYearsExperience()
                + 1200.0 * getAlbumsSold()
                + 500000.0 * getPerformances()
                + 500000.0 * gameShows;
    }

    @Override
    public String getType() {
        return "Famous";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | game shows=" + gameShows);
    }
}

public class Exercise4_SingerSalaryManagement {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<Singer> SINGERS = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    addRegularSinger();
                    break;
                case 2:
                    addFamousSinger();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    showTotalSalary();
                    break;
                case 5:
                    showHighestPaidSinger();
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
        System.out.println("=== Singer Salary Management for XYZ Company ===");
        System.out.println("1. Add regular singer");
        System.out.println("2. Add famous singer");
        System.out.println("3. Display all singers");
        System.out.println("4. Calculate total salary");
        System.out.println("5. Find the highest-paid singer");
        System.out.println("0. Exit");
    }

    private static void addRegularSinger() {
        String name = readNonEmpty("Full name: ");
        if (findByName(name) != null) {
            System.out.println("Duplicate singer name.");
            return;
        }
        int years = readIntMin("Years of experience: ", 0);
        int albums = readIntMin("Albums sold: ", 0);
        int performances = readIntMin("Performances: ", 0);
        try {
            SINGERS.add(new RegularSinger(name, years, albums, performances));
            System.out.println("Regular singer added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void addFamousSinger() {
        String name = readNonEmpty("Full name: ");
        if (findByName(name) != null) {
            System.out.println("Duplicate singer name.");
            return;
        }
        int years = readIntMin("Years of experience: ", 0);
        int albums = readIntMin("Albums sold: ", 0);
        int performances = readIntMin("Performances: ", 0);
        int gameShows = readIntMin("Game shows attended: ", 0);
        try {
            SINGERS.add(new FamousSinger(name, years, albums, performances, gameShows));
            System.out.println("Famous singer added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void displayAll() {
        if (SINGERS.isEmpty()) {
            System.out.println("No singers.");
            return;
        }
        for (Singer singer : SINGERS) {
            singer.displayInfo();
        }
    }

    private static void showTotalSalary() {
        if (SINGERS.isEmpty()) {
            System.out.println("No singers.");
            return;
        }
        double total = 0;
        for (Singer singer : SINGERS) {
            total += singer.calculateSalary();
        }
        System.out.println("Total salary: " + money(total) + " VND");
    }

    private static void showHighestPaidSinger() {
        if (SINGERS.isEmpty()) {
            System.out.println("No singers.");
            return;
        }
        Singer highest = SINGERS.get(0);
        for (Singer singer : SINGERS) {
            if (singer.calculateSalary() > highest.calculateSalary()) {
                highest = singer;
            }
        }
        System.out.println("Highest-paid singer:");
        highest.displayInfo();
    }

    private static Singer findByName(String name) {
        for (Singer singer : SINGERS) {
            if (singer.getFullName().equalsIgnoreCase(name.trim())) {
                return singer;
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
