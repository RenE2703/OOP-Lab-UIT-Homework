import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

abstract class Delivery {
    private static int createdCount = 0;

    private String orderId;
    private String customerName;
    private double distance;
    private double weight;

    public Delivery(String orderId, String customerName, double distance, double weight) {
        setOrderId(orderId);
        setCustomerName(customerName);
        setDistance(distance);
        setWeight(weight);
        createdCount++;
    }

    public Delivery(String orderId, double distance, double weight) {
        this(orderId, "Walk-in Customer", distance, weight);
    }

    public static int getCreatedCount() {
        return createdCount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID must not be empty");
        }
        this.orderId = orderId.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name must not be empty");
        }
        this.customerName = customerName.trim();
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0");
        }
        this.distance = distance;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0");
        }
        this.weight = weight;
    }

    public abstract double calculateShippingFee();

    public abstract String getType();

    public void displayInfo() {
        System.out.print("[" + getType() + "] ID=" + orderId + " | " + customerName
                + " | distance=" + distance + " km | weight=" + weight + " kg");
    }

    protected String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }
}

class StandardDelivery extends Delivery {
    public StandardDelivery(String orderId, String customerName, double distance, double weight) {
        super(orderId, customerName, distance, weight);
    }

    public StandardDelivery(String orderId, double distance, double weight) {
        super(orderId, distance, weight);
    }

    @Override
    public double calculateShippingFee() {
        return 15000.0 + 3000.0 * getDistance() + 2000.0 * getWeight();
    }

    @Override
    public String getType() {
        return "Standard";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | fee=" + money(calculateShippingFee()) + " VND");
    }
}

class ExpressDelivery extends Delivery {
    public ExpressDelivery(String orderId, String customerName, double distance, double weight) {
        super(orderId, customerName, distance, weight);
    }

    @Override
    public double calculateShippingFee() {
        return 30000.0 + 5000.0 * getDistance() + 4000.0 * getWeight();
    }

    @Override
    public String getType() {
        return "Express";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | fee=" + money(calculateShippingFee()) + " VND");
    }
}

public class Exercise3_OnlineOrderDeliveryManagement {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<Delivery> DELIVERIES = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    addStandardDelivery();
                    break;
                case 2:
                    addExpressDelivery();
                    break;
                case 3:
                    displayAll();
                    break;
                case 4:
                    searchById();
                    break;
                case 5:
                    showAboveThreshold();
                    break;
                case 6:
                    showStatistics();
                    break;
                case 7:
                    showCreatedCount();
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
        System.out.println("=== Online Order Delivery Management ===");
        System.out.println("1. Add standard delivery");
        System.out.println("2. Add express delivery");
        System.out.println("3. Display all deliveries");
        System.out.println("4. Search by order ID");
        System.out.println("5. Show deliveries above a fee threshold");
        System.out.println("6. Show statistics");
        System.out.println("7. Show created delivery counter");
        System.out.println("0. Exit");
    }

    private static void addStandardDelivery() {
        String id = readNonEmpty("Order ID: ");
        if (findById(id) != null) {
            System.out.println("Duplicate order ID.");
            return;
        }
        String customer = readNonEmpty("Customer name: ");
        double distance = readPositiveDouble("Distance (km): ");
        double weight = readPositiveDouble("Weight (kg): ");
        try {
            DELIVERIES.add(new StandardDelivery(id, customer, distance, weight));
            System.out.println("Standard delivery added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void addExpressDelivery() {
        String id = readNonEmpty("Order ID: ");
        if (findById(id) != null) {
            System.out.println("Duplicate order ID.");
            return;
        }
        String customer = readNonEmpty("Customer name: ");
        double distance = readPositiveDouble("Distance (km): ");
        double weight = readPositiveDouble("Weight (kg): ");
        try {
            DELIVERIES.add(new ExpressDelivery(id, customer, distance, weight));
            System.out.println("Express delivery added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void displayAll() {
        if (DELIVERIES.isEmpty()) {
            System.out.println("No deliveries.");
            return;
        }
        for (Delivery delivery : DELIVERIES) {
            delivery.displayInfo();
        }
    }

    private static void searchById() {
        if (DELIVERIES.isEmpty()) {
            System.out.println("No deliveries.");
            return;
        }
        String id = readNonEmpty("Order ID to search: ");
        Delivery delivery = findById(id);
        if (delivery == null) {
            System.out.println("No delivery found.");
            return;
        }
        delivery.displayInfo();
    }

    private static void showAboveThreshold() {
        if (DELIVERIES.isEmpty()) {
            System.out.println("No deliveries.");
            return;
        }
        double threshold = readDoubleMin("Fee threshold (VND): ", 0);
        boolean found = false;
        for (Delivery delivery : DELIVERIES) {
            if (delivery.calculateShippingFee() > threshold) {
                delivery.displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No delivery exceeds the threshold.");
        }
    }

    private static void showStatistics() {
        if (DELIVERIES.isEmpty()) {
            System.out.println("No deliveries.");
            return;
        }
        int standard = 0;
        int express = 0;
        double totalRevenue = 0;
        for (Delivery delivery : DELIVERIES) {
            if (delivery instanceof StandardDelivery) {
                standard++;
            } else {
                express++;
            }
            totalRevenue += delivery.calculateShippingFee();
        }
        System.out.println("Standard deliveries: " + standard);
        System.out.println("Express deliveries: " + express);
        System.out.println("Total shipping revenue: " + money(totalRevenue) + " VND");
    }

    private static void showCreatedCount() {
        System.out.println("Delivery objects created: " + Delivery.getCreatedCount());
        System.out.println("The counter is static because it belongs to the Delivery class "
                + "and is shared by every delivery object.");
    }

    private static Delivery findById(String id) {
        for (Delivery delivery : DELIVERIES) {
            if (delivery.getOrderId().equalsIgnoreCase(id.trim())) {
                return delivery;
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

    private static double readPositiveDouble(String label) {
        while (true) {
            System.out.print(label);
            String line = SCANNER.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
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
