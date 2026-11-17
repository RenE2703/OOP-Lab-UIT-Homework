import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

abstract class Ticket {
    private String id;
    private String movieTitle;
    private int quantity;

    public Ticket(String id, String movieTitle, int quantity) {
        setId(id);
        setMovieTitle(movieTitle);
        setQuantity(quantity);
    }

    public Ticket(String id, String movieTitle) {
        this(id, movieTitle, 1);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket ID must not be empty");
        }
        this.id = id.trim();
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        if (movieTitle == null || movieTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title must not be empty");
        }
        this.movieTitle = movieTitle.trim();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        this.quantity = quantity;
    }

    public abstract double getUnitPrice();

    public abstract double calculateTotalPrice();

    public abstract String getType();

    public double getDiscountAmount() {
        return getUnitPrice() * quantity - calculateTotalPrice();
    }

    public void displayInfo() {
        System.out.println("[" + getType() + "] ID=" + id + " | movie=" + movieTitle
                + " | qty=" + quantity + " | unit=" + money(getUnitPrice())
                + " VND | discount=" + money(getDiscountAmount())
                + " VND | total=" + money(calculateTotalPrice()) + " VND");
    }

    protected String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }
}

class RegularTicket extends Ticket {
    private static final double UNIT_PRICE = 80000.0;

    public RegularTicket(String id, String movieTitle, int quantity) {
        super(id, movieTitle, quantity);
    }

    public RegularTicket(String id, String movieTitle) {
        this(id, movieTitle, 1);
    }

    @Override
    public double getUnitPrice() {
        return UNIT_PRICE;
    }

    @Override
    public double calculateTotalPrice() {
        double subtotal = getUnitPrice() * getQuantity();
        if (getQuantity() >= 5) {
            return subtotal * 0.9;
        }
        return subtotal;
    }

    @Override
    public String getType() {
        return "Regular";
    }
}

class VIPTicket extends Ticket {
    private static final double UNIT_PRICE = 150000.0;

    public VIPTicket(String id, String movieTitle, int quantity) {
        super(id, movieTitle, quantity);
    }

    @Override
    public double getUnitPrice() {
        return UNIT_PRICE;
    }

    @Override
    public double calculateTotalPrice() {
        double subtotal = getUnitPrice() * getQuantity();
        if (getQuantity() >= 3) {
            return subtotal * 0.85;
        }
        return subtotal;
    }

    @Override
    public String getType() {
        return "VIP";
    }
}

public class Exercise4_CinemaTicketSalesManagement {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<Ticket> TICKETS = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    addRegularTicket();
                    break;
                case 2:
                    addVipTicket();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    searchByMovieTitle();
                    break;
                case 5:
                    displaySortedByTotal();
                    break;
                case 6:
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
        System.out.println("=== Cinema Ticket Sales Management ===");
        System.out.println("1. Add regular ticket");
        System.out.println("2. Add VIP ticket");
        System.out.println("3. Display all purchases");
        System.out.println("4. Search by movie title");
        System.out.println("5. Display purchases sorted by total (desc)");
        System.out.println("6. Show statistics");
        System.out.println("0. Exit");
    }

    private static void addRegularTicket() {
        String id = readNonEmpty("Ticket ID: ");
        if (findById(id) != null) {
            System.out.println("Duplicate ticket ID.");
            return;
        }
        String movie = readNonEmpty("Movie title: ");
        int quantity = readIntMin("Quantity: ", 1);
        try {
            TICKETS.add(new RegularTicket(id, movie, quantity));
            System.out.println("Regular ticket added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void addVipTicket() {
        String id = readNonEmpty("Ticket ID: ");
        if (findById(id) != null) {
            System.out.println("Duplicate ticket ID.");
            return;
        }
        String movie = readNonEmpty("Movie title: ");
        int quantity = readIntMin("Quantity: ", 1);
        try {
            TICKETS.add(new VIPTicket(id, movie, quantity));
            System.out.println("VIP ticket added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void displayAll() {
        if (TICKETS.isEmpty()) {
            System.out.println("No tickets.");
            return;
        }
        for (Ticket ticket : TICKETS) {
            ticket.displayInfo();
        }
    }

    private static void searchByMovieTitle() {
        if (TICKETS.isEmpty()) {
            System.out.println("No tickets.");
            return;
        }
        String keyword = readNonEmpty("Movie title to search: ").toLowerCase(Locale.US);
        boolean found = false;
        for (Ticket ticket : TICKETS) {
            if (ticket.getMovieTitle().toLowerCase(Locale.US).contains(keyword)) {
                ticket.displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No ticket found.");
        }
    }

    private static void displaySortedByTotal() {
        if (TICKETS.isEmpty()) {
            System.out.println("No tickets.");
            return;
        }
        List<Ticket> sorted = new ArrayList<>(TICKETS);
        sorted.sort(Comparator.comparingDouble(Ticket::calculateTotalPrice).reversed());
        for (Ticket ticket : sorted) {
            ticket.displayInfo();
        }
    }

    private static void showStatistics() {
        if (TICKETS.isEmpty()) {
            System.out.println("No tickets.");
            return;
        }
        double totalRevenue = 0;
        Map<String, Double> revenueByMovie = new LinkedHashMap<>();
        Map<String, String> titles = new LinkedHashMap<>();
        for (Ticket ticket : TICKETS) {
            double total = ticket.calculateTotalPrice();
            totalRevenue += total;
            String key = ticket.getMovieTitle().toLowerCase(Locale.US);
            titles.putIfAbsent(key, ticket.getMovieTitle());
            revenueByMovie.merge(key, total, Double::sum);
        }
        String bestKey = null;
        double bestRevenue = 0;
        for (Map.Entry<String, Double> entry : revenueByMovie.entrySet()) {
            if (bestKey == null || entry.getValue() > bestRevenue) {
                bestKey = entry.getKey();
                bestRevenue = entry.getValue();
            }
        }
        System.out.println("Total revenue: " + money(totalRevenue) + " VND");
        System.out.println("Highest revenue movie: " + titles.get(bestKey)
                + " (" + money(bestRevenue) + " VND)");
    }

    private static Ticket findById(String id) {
        for (Ticket ticket : TICKETS) {
            if (ticket.getId().equalsIgnoreCase(id.trim())) {
                return ticket;
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
