import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

abstract class Ticket {
    private String ticketId;
    private String holderName;
    private int birthYear;
    private int games;

    public Ticket(String ticketId, String holderName, int birthYear, int games) {
        setTicketId(ticketId);
        setHolderName(holderName);
        setBirthYear(birthYear);
        setGames(games);
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket ID must not be empty");
        }
        this.ticketId = ticketId.trim();
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        if (holderName == null || holderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Holder name must not be empty");
        }
        this.holderName = holderName.trim();
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        int currentYear = Year.now().getValue();
        if (birthYear < 1900 || birthYear > currentYear) {
            throw new IllegalArgumentException("Birth year must be between 1900 and " + currentYear);
        }
        this.birthYear = birthYear;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        if (games < 0) {
            throw new IllegalArgumentException("Number of games must not be negative");
        }
        this.games = games;
    }

    public abstract double calculatePrice();

    public abstract String getType();

    public void displayInfo() {
        System.out.print("[" + getType() + "] ID=" + ticketId + " | " + holderName
                + " | birth year=" + birthYear + " | games=" + games
                + " | price=" + money(calculatePrice()) + " VND");
    }

    protected String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }
}

class FullPackageTicket extends Ticket {
    private static final double PRICE = 200000.0;

    public FullPackageTicket(String ticketId, String holderName, int birthYear, int games) {
        super(ticketId, holderName, birthYear, games);
    }

    @Override
    public double calculatePrice() {
        return PRICE;
    }

    @Override
    public String getType() {
        return "FullPackage";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | access to all 30 games");
    }
}

class PartialTicket extends Ticket {
    private static final double ENTRANCE_FEE = 70000.0;
    private static final double GAME_FEE = 20000.0;

    public PartialTicket(String ticketId, String holderName, int birthYear, int games) {
        super(ticketId, holderName, birthYear, games);
    }

    @Override
    public double calculatePrice() {
        return ENTRANCE_FEE + GAME_FEE * getGames();
    }

    @Override
    public String getType() {
        return "Partial";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | entrance fee=" + money(ENTRANCE_FEE)
                + " VND | game fee=" + money(GAME_FEE) + " VND/game");
    }
}

public class Exercise2_TicketManagementDamSenPark {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<Ticket> TICKETS = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    addFullPackageTicket();
                    break;
                case 2:
                    addPartialTicket();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    showTotalRevenue();
                    break;
                case 5:
                    countPartialTickets();
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
        System.out.println("=== Ticket Management for Dam Sen Park ===");
        System.out.println("1. Add full package ticket");
        System.out.println("2. Add partial ticket");
        System.out.println("3. Display all tickets");
        System.out.println("4. Calculate total revenue");
        System.out.println("5. Count partial tickets");
        System.out.println("0. Exit");
    }

    private static void addFullPackageTicket() {
        String id = readNonEmpty("Ticket ID: ");
        if (findById(id) != null) {
            System.out.println("Duplicate ticket ID.");
            return;
        }
        String name = readNonEmpty("Holder name: ");
        int birthYear = readIntRange("Year of birth: ", 1900, Year.now().getValue());
        int games = readIntMin("Number of games played: ", 0);
        try {
            TICKETS.add(new FullPackageTicket(id, name, birthYear, games));
            System.out.println("Full package ticket added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void addPartialTicket() {
        String id = readNonEmpty("Ticket ID: ");
        if (findById(id) != null) {
            System.out.println("Duplicate ticket ID.");
            return;
        }
        String name = readNonEmpty("Holder name: ");
        int birthYear = readIntRange("Year of birth: ", 1900, Year.now().getValue());
        int games = readIntMin("Number of games played: ", 0);
        try {
            TICKETS.add(new PartialTicket(id, name, birthYear, games));
            System.out.println("Partial ticket added.");
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

    private static void showTotalRevenue() {
        if (TICKETS.isEmpty()) {
            System.out.println("No tickets.");
            return;
        }
        double total = 0;
        for (Ticket ticket : TICKETS) {
            total += ticket.calculatePrice();
        }
        System.out.println("Total revenue: " + money(total) + " VND");
    }

    private static void countPartialTickets() {
        if (TICKETS.isEmpty()) {
            System.out.println("No tickets.");
            return;
        }
        int partial = 0;
        for (Ticket ticket : TICKETS) {
            if (ticket instanceof PartialTicket) {
                partial++;
            }
        }
        System.out.println("Partial tickets sold: " + partial + "/" + TICKETS.size());
    }

    private static Ticket findById(String id) {
        for (Ticket ticket : TICKETS) {
            if (ticket.getTicketId().equalsIgnoreCase(id.trim())) {
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

    private static int readIntRange(String label, int min, int max) {
        while (true) {
            int value = readInt(label);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Value must be between " + min + " and " + max + ".");
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
