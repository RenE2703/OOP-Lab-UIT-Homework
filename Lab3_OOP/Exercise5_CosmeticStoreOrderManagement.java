import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

abstract class Product {
    private String productId;
    private int volume;

    public Product(String productId, int volume) {
        setProductId(productId);
        setVolume(volume);
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID must not be empty");
        }
        this.productId = productId.trim();
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        if (volume < 1) {
            throw new IllegalArgumentException("Volume must be at least 1 ml");
        }
        this.volume = volume;
    }

    public abstract String getProductName();

    public abstract double getPricePerMl();

    public double calculatePrice() {
        return volume * getPricePerMl();
    }

    public void displayInfo() {
        System.out.println("  " + getProductName() + " | volume=" + volume + " ml"
                + " | price/ml=" + money(getPricePerMl())
                + " VND | price=" + money(calculatePrice()) + " VND");
    }

    protected String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }
}

class Perfume extends Product {
    public static final String ROSE = "Rose";
    public static final String CHAMOMILE = "Chamomile";

    private String scent;

    public Perfume(String productId, int volume, String scent) {
        super(productId, volume);
        setScent(scent);
    }

    public String getScent() {
        return scent;
    }

    public void setScent(String scent) {
        if (ROSE.equalsIgnoreCase(scent)) {
            this.scent = ROSE;
        } else if (CHAMOMILE.equalsIgnoreCase(scent)) {
            this.scent = CHAMOMILE;
        } else {
            throw new IllegalArgumentException("Perfume must be Rose or Chamomile");
        }
    }

    public boolean isRose() {
        return ROSE.equals(scent);
    }

    @Override
    public String getProductName() {
        return scent + " Perfume";
    }

    @Override
    public double getPricePerMl() {
        return isRose() ? 20000.0 : 15000.0;
    }
}

class Shampoo extends Product {
    public static final String DRY_HAIR = "Dry Hair";
    public static final String OILY_STANDARD_1 = "Oily Hair Standard 1";
    public static final String OILY_STANDARD_2 = "Oily Hair Standard 2";

    private String variant;

    public Shampoo(String productId, int volume, String variant) {
        super(productId, volume);
        setVariant(variant);
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        if (DRY_HAIR.equalsIgnoreCase(variant)) {
            this.variant = DRY_HAIR;
        } else if (OILY_STANDARD_1.equalsIgnoreCase(variant)) {
            this.variant = OILY_STANDARD_1;
        } else if (OILY_STANDARD_2.equalsIgnoreCase(variant)) {
            this.variant = OILY_STANDARD_2;
        } else {
            throw new IllegalArgumentException("Unknown shampoo variant");
        }
    }

    public boolean isOilyHair() {
        return variant.startsWith("Oily Hair");
    }

    @Override
    public String getProductName() {
        return variant + " Shampoo";
    }

    @Override
    public double getPricePerMl() {
        if (variant.equals(DRY_HAIR)) {
            return 20000.0;
        }
        if (variant.equals(OILY_STANDARD_1)) {
            return 30000.0;
        }
        return 40000.0;
    }
}

class ShowerGel extends Product {
    public static final String DRY_SKIN = "Dry Skin";
    public static final String OILY_SKIN = "Oily Skin";

    private String skinType;

    public ShowerGel(String productId, int volume, String skinType) {
        super(productId, volume);
        setSkinType(skinType);
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        if (DRY_SKIN.equalsIgnoreCase(skinType)) {
            this.skinType = DRY_SKIN;
        } else if (OILY_SKIN.equalsIgnoreCase(skinType)) {
            this.skinType = OILY_SKIN;
        } else {
            throw new IllegalArgumentException("Shower gel must be Dry Skin or Oily Skin");
        }
    }

    @Override
    public String getProductName() {
        return skinType + " Shower Gel";
    }

    @Override
    public double getPricePerMl() {
        return skinType.equals(DRY_SKIN) ? 20000.0 : 40000.0;
    }
}

abstract class CosmeticPackage {
    private String packageName;
    private List<Product> products;

    public CosmeticPackage(String packageName) {
        this.packageName = packageName;
        this.products = new ArrayList<>();
    }

    public String getPackageName() {
        return packageName;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        products.add(product);
    }

    public double calculatePrice() {
        double total = 0;
        for (Product product : products) {
            total += product.calculatePrice();
        }
        return total;
    }

    public abstract boolean meetsRequirement();

    public void displayInfo() {
        System.out.println("Package: " + packageName + " | requirement met=" + meetsRequirement());
        for (Product product : products) {
            product.displayInfo();
        }
        System.out.println("  Package price: " + money(calculatePrice()) + " VND");
    }

    protected String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }
}

class RomanticPackage extends CosmeticPackage {
    public RomanticPackage() {
        super("Romantic Package");
    }

    @Override
    public boolean meetsRequirement() {
        for (Product product : getProducts()) {
            if (product instanceof Perfume && ((Perfume) product).isRose()) {
                return true;
            }
        }
        return false;
    }
}

class FreshAirPackage extends CosmeticPackage {
    public FreshAirPackage() {
        super("Fresh-Air Package");
    }

    @Override
    public boolean meetsRequirement() {
        for (Product product : getProducts()) {
            if (product instanceof Shampoo && ((Shampoo) product).isOilyHair()) {
                return true;
            }
        }
        return false;
    }
}

class Customer {
    private String customerId;
    private String fullName;
    private String address;
    private String phoneNumber;

    public Customer(String customerId, String fullName, String address, String phoneNumber) {
        setCustomerId(customerId);
        setFullName(fullName);
        setAddress(address);
        setPhoneNumber(phoneNumber);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID must not be empty");
        }
        this.customerId = customerId.trim();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.trim().matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Phone number must contain 10 or 11 digits");
        }
        this.phoneNumber = phoneNumber.trim();
    }

    public void displayInfo() {
        System.out.println("Customer: ID=" + customerId + " | " + fullName
                + " | address=" + address + " | phone=" + phoneNumber);
    }
}

class Order {
    private String orderId;
    private Customer customer;
    private String invoiceDate;
    private CosmeticPackage cosmeticPackage;

    public Order(String orderId, Customer customer, String invoiceDate,
            CosmeticPackage cosmeticPackage) {
        setOrderId(orderId);
        setCustomer(customer);
        setInvoiceDate(invoiceDate);
        setCosmeticPackage(cosmeticPackage);
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer must not be null");
        }
        this.customer = customer;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        if (invoiceDate == null || invoiceDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Invoice date must not be empty");
        }
        this.invoiceDate = invoiceDate.trim();
    }

    public CosmeticPackage getCosmeticPackage() {
        return cosmeticPackage;
    }

    public void setCosmeticPackage(CosmeticPackage cosmeticPackage) {
        if (cosmeticPackage == null) {
            throw new IllegalArgumentException("Package must not be null");
        }
        this.cosmeticPackage = cosmeticPackage;
    }

    public double getOrderPrice() {
        return cosmeticPackage.calculatePrice();
    }

    public void displayInfo() {
        System.out.println("Order ID: " + orderId + " | invoice date=" + invoiceDate);
        customer.displayInfo();
        cosmeticPackage.displayInfo();
        System.out.println("Order price: " + money(getOrderPrice()) + " VND");
    }

    private String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }
}

public class Exercise5_CosmeticStoreOrderManagement {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<Order> ORDERS = new ArrayList<>();
    private static final String FILE_NAME = "order_list.txt";

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    addOrder();
                    break;
                case 2:
                    displayAllOrders();
                    break;
                case 3:
                    saveOrders();
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
        System.out.println("=== Cosmetic Store Order Management ===");
        System.out.println("1. Add order");
        System.out.println("2. Display all orders");
        System.out.println("3. Save order list to " + FILE_NAME);
        System.out.println("0. Exit");
    }

    private static void addOrder() {
        String orderId = readNonEmpty("Order ID: ");
        if (findOrder(orderId) != null) {
            System.out.println("Duplicate order ID.");
            return;
        }
        String customerId = readNonEmpty("Customer ID: ");
        String customerName = readNonEmpty("Customer full name: ");
        String address = readNonEmpty("Address: ");
        String phone = readPhoneNumber();
        String invoiceDate = readNonEmpty("Invoice date: ");
        try {
            Customer customer = new Customer(customerId, customerName, address, phone);
            CosmeticPackage cosmeticPackage = buildPackage(orderId);
            if (!cosmeticPackage.meetsRequirement()) {
                System.out.println("Rejected: package does not include its required product.");
                return;
            }
            ORDERS.add(new Order(orderId, customer, invoiceDate, cosmeticPackage));
            System.out.println("Order added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static CosmeticPackage buildPackage(String orderId) {
        int type = readIntRange("Package type (1 Romantic, 2 Fresh-Air): ", 1, 2);
        CosmeticPackage cosmeticPackage;
        if (type == 1) {
            cosmeticPackage = new RomanticPackage();
            int roseVolume = readIntMin("Rose Perfume volume (ml): ", 1);
            cosmeticPackage.addProduct(new Perfume(orderId + "-P1", roseVolume, Perfume.ROSE));
            int shampooVolume = readIntMin("Dry Hair Shampoo volume (ml, 0 to skip): ", 0);
            if (shampooVolume > 0) {
                cosmeticPackage.addProduct(new Shampoo(orderId + "-P2", shampooVolume,
                        Shampoo.DRY_HAIR));
            }
            int gelType = readIntRange("Shower Gel type (0 skip, 1 Dry Skin, 2 Oily Skin): ", 0, 2);
            if (gelType > 0) {
                int gelVolume = readIntMin("Shower Gel volume (ml): ", 1);
                String skinType = gelType == 1 ? ShowerGel.DRY_SKIN : ShowerGel.OILY_SKIN;
                cosmeticPackage.addProduct(new ShowerGel(orderId + "-P3", gelVolume, skinType));
            }
        } else {
            cosmeticPackage = new FreshAirPackage();
            int variant = readIntRange(
                    "Oily Hair Shampoo variant (1 Standard 1, 2 Standard 2): ", 1, 2);
            int shampooVolume = readIntMin("Oily Hair Shampoo volume (ml): ", 1);
            String shampooVariant = variant == 1 ? Shampoo.OILY_STANDARD_1 : Shampoo.OILY_STANDARD_2;
            cosmeticPackage.addProduct(new Shampoo(orderId + "-P1", shampooVolume, shampooVariant));
            int perfumeType = readIntRange("Perfume type (0 skip, 1 Rose, 2 Chamomile): ", 0, 2);
            if (perfumeType > 0) {
                int perfumeVolume = readIntMin("Perfume volume (ml): ", 1);
                String scent = perfumeType == 1 ? Perfume.ROSE : Perfume.CHAMOMILE;
                cosmeticPackage.addProduct(new Perfume(orderId + "-P2", perfumeVolume, scent));
            }
            int gelType = readIntRange("Shower Gel type (0 skip, 1 Dry Skin, 2 Oily Skin): ", 0, 2);
            if (gelType > 0) {
                int gelVolume = readIntMin("Shower Gel volume (ml): ", 1);
                String skinType = gelType == 1 ? ShowerGel.DRY_SKIN : ShowerGel.OILY_SKIN;
                cosmeticPackage.addProduct(new ShowerGel(orderId + "-P3", gelVolume, skinType));
            }
        }
        return cosmeticPackage;
    }

    private static void displayAllOrders() {
        if (ORDERS.isEmpty()) {
            System.out.println("No orders.");
            return;
        }
        double total = 0;
        for (Order order : ORDERS) {
            order.displayInfo();
            System.out.println();
            total += order.getOrderPrice();
        }
        System.out.println("Total revenue: " + money(total) + " VND");
    }

    private static void saveOrders() {
        if (ORDERS.isEmpty()) {
            System.out.println("No orders.");
            return;
        }
        displayAllOrders();
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(FILE_NAME),
                StandardCharsets.UTF_8)) {
            double total = 0;
            for (Order order : ORDERS) {
                Customer customer = order.getCustomer();
                CosmeticPackage cosmeticPackage = order.getCosmeticPackage();
                writer.write("Order ID: " + order.getOrderId()
                        + " | invoice date: " + order.getInvoiceDate());
                writer.newLine();
                writer.write("Customer ID: " + customer.getCustomerId()
                        + " | name: " + customer.getFullName()
                        + " | address: " + customer.getAddress()
                        + " | phone: " + customer.getPhoneNumber());
                writer.newLine();
                writer.write("Package: " + cosmeticPackage.getPackageName());
                writer.newLine();
                for (Product product : cosmeticPackage.getProducts()) {
                    writer.write("  " + product.getProductName()
                            + " | volume: " + product.getVolume() + " ml"
                            + " | price per ml: " + money(product.getPricePerMl()) + " VND"
                            + " | price: " + money(product.calculatePrice()) + " VND");
                    writer.newLine();
                }
                writer.write("Order price: " + money(order.getOrderPrice()) + " VND");
                writer.newLine();
                writer.newLine();
                total += order.getOrderPrice();
            }
            writer.write("Total orders: " + ORDERS.size());
            writer.newLine();
            writer.write("Total revenue: " + money(total) + " VND");
            writer.newLine();
            System.out.println("Saved " + ORDERS.size() + " order(s) to " + FILE_NAME + ".");
        } catch (IOException e) {
            System.out.println("Failed to save order list: " + e.getMessage());
        }
    }

    private static Order findOrder(String orderId) {
        for (Order order : ORDERS) {
            if (order.getOrderId().equalsIgnoreCase(orderId.trim())) {
                return order;
            }
        }
        return null;
    }

    private static String readPhoneNumber() {
        while (true) {
            String phone = readNonEmpty("Phone number: ");
            if (phone.matches("\\d{10,11}")) {
                return phone;
            }
            System.out.println("Phone number must contain 10 or 11 digits.");
        }
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
