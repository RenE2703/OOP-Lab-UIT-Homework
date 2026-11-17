import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

abstract class Person {
    private String fullName;
    private int age;

    public Person(String fullName, int age) {
        setFullName(fullName);
        setAge(age);
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 1 || age > 120) {
            throw new IllegalArgumentException("Age must be between 1 and 120");
        }
        this.age = age;
    }

    public abstract String getRole();

    public void displayInfo() {
        System.out.print("[" + getRole() + "] " + fullName + " | age=" + age);
    }
}

class Guest extends Person {
    private String citizenId;

    public Guest(String citizenId, String fullName, int age) {
        super(fullName, age);
        setCitizenId(citizenId);
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        if (citizenId == null || !citizenId.trim().matches("\\d{9}|\\d{12}")) {
            throw new IllegalArgumentException("Citizen ID must contain 9 or 12 digits");
        }
        this.citizenId = citizenId.trim();
    }

    @Override
    public String getRole() {
        return "Guest";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | CCCD=" + citizenId);
    }
}

class Staff extends Person {
    private String licenseId;
    private String employeeId;

    public Staff(String licenseId, String employeeId, String fullName, int age) {
        super(fullName, age);
        setLicenseId(licenseId);
        setEmployeeId(employeeId);
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        if (licenseId == null || licenseId.trim().isEmpty()) {
            throw new IllegalArgumentException("License ID must not be empty");
        }
        this.licenseId = licenseId.trim();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID must not be empty");
        }
        this.employeeId = employeeId.trim();
    }

    @Override
    public String getRole() {
        return "Staff";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(" | license ID=" + licenseId + " | employee ID=" + employeeId);
    }
}

class Room {
    private String roomId;
    private String roomType;
    private int rentalDays;
    private Guest guest;

    public Room(String roomId, String roomType, int rentalDays, Guest guest) {
        setRoomId(roomId);
        setRoomType(roomType);
        setRentalDays(rentalDays);
        setGuest(guest);
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID must not be empty");
        }
        this.roomId = roomId.trim();
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        String value = roomType == null ? "" : roomType.trim().toUpperCase(Locale.US);
        if (!value.equals("VIP1") && !value.equals("VIP2") && !value.equals("VIP3")) {
            throw new IllegalArgumentException("Room type must be VIP1, VIP2 or VIP3");
        }
        this.roomType = value;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental days must be at least 1");
        }
        this.rentalDays = rentalDays;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest must not be null");
        }
        this.guest = guest;
    }

    public double getDailyPrice() {
        if (roomType.equals("VIP1")) {
            return 900000.0;
        }
        if (roomType.equals("VIP2")) {
            return 700000.0;
        }
        return 500000.0;
    }

    public double calculateCost() {
        return getDailyPrice() * rentalDays;
    }

    public void displayInfo() {
        System.out.print("Room " + roomId + " | type=" + roomType
                + " | days=" + rentalDays + " | daily price=" + money(getDailyPrice())
                + " VND | cost=" + money(calculateCost()) + " VND | guest: ");
        guest.displayInfo();
    }

    private String money(double amount) {
        return String.format(Locale.US, "%,.0f", amount);
    }
}

public class Exercise3_HotelRoomRentalManagement {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<Guest> GUESTS = new ArrayList<>();
    private static final List<Staff> STAFF = new ArrayList<>();
    private static final List<Room> ROOMS = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1:
                    addGuest();
                    break;
                case 2:
                    addStaff();
                    break;
                case 3:
                    rentRoom();
                    break;
                case 4:
                    removeGuest();
                    break;
                case 5:
                    displayGuests();
                    break;
                case 6:
                    displayStaff();
                    break;
                case 7:
                    displayRooms();
                    break;
                case 8:
                    searchGuest();
                    break;
                case 9:
                    showTotalRevenue();
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
        System.out.println("=== Hotel Room Rental Management ===");
        System.out.println("1. Add guest");
        System.out.println("2. Add staff");
        System.out.println("3. Rent a room");
        System.out.println("4. Remove guest");
        System.out.println("5. Display guests");
        System.out.println("6. Display staff");
        System.out.println("7. Display rooms");
        System.out.println("8. Search guest by citizen ID");
        System.out.println("9. Calculate total revenue");
        System.out.println("0. Exit");
    }

    private static void addGuest() {
        String citizenId = readNonEmpty("Citizen ID (CCCD): ");
        if (findGuest(citizenId) != null) {
            System.out.println("Duplicate citizen ID.");
            return;
        }
        String name = readNonEmpty("Full name: ");
        int age = readIntRange("Age: ", 1, 120);
        try {
            GUESTS.add(new Guest(citizenId, name, age));
            System.out.println("Guest added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void addStaff() {
        String employeeId = readNonEmpty("Employee ID: ");
        for (Staff staff : STAFF) {
            if (staff.getEmployeeId().equalsIgnoreCase(employeeId)) {
                System.out.println("Duplicate employee ID.");
                return;
            }
        }
        String licenseId = readNonEmpty("License ID: ");
        String name = readNonEmpty("Full name: ");
        int age = readIntRange("Age: ", 1, 120);
        try {
            STAFF.add(new Staff(licenseId, employeeId, name, age));
            System.out.println("Staff added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void rentRoom() {
        if (GUESTS.isEmpty()) {
            System.out.println("No guests.");
            return;
        }
        String citizenId = readNonEmpty("Guest citizen ID: ");
        Guest guest = findGuest(citizenId);
        if (guest == null) {
            System.out.println("No guest found.");
            return;
        }
        for (Room room : ROOMS) {
            if (room.getGuest().getCitizenId().equalsIgnoreCase(guest.getCitizenId())) {
                System.out.println("This guest is already renting a room.");
                return;
            }
        }
        String roomId = readNonEmpty("Room ID: ");
        for (Room room : ROOMS) {
            if (room.getRoomId().equalsIgnoreCase(roomId)) {
                System.out.println("Duplicate room ID.");
                return;
            }
        }
        String roomType = readRoomType();
        int days = readIntMin("Number of rental days: ", 1);
        try {
            ROOMS.add(new Room(roomId, roomType, days, guest));
            System.out.println("Room rented.");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static void removeGuest() {
        if (GUESTS.isEmpty()) {
            System.out.println("No guests.");
            return;
        }
        String citizenId = readNonEmpty("Guest citizen ID to remove: ");
        Guest guest = findGuest(citizenId);
        if (guest == null) {
            System.out.println("No guest found.");
            return;
        }
        ROOMS.removeIf(room -> room.getGuest().getCitizenId()
                .equalsIgnoreCase(guest.getCitizenId()));
        GUESTS.remove(guest);
        System.out.println("Guest removed.");
    }

    private static void displayGuests() {
        if (GUESTS.isEmpty()) {
            System.out.println("No guests.");
            return;
        }
        for (Guest guest : GUESTS) {
            guest.displayInfo();
        }
    }

    private static void displayStaff() {
        if (STAFF.isEmpty()) {
            System.out.println("No staff.");
            return;
        }
        for (Staff staff : STAFF) {
            staff.displayInfo();
        }
    }

    private static void displayRooms() {
        if (ROOMS.isEmpty()) {
            System.out.println("No rooms rented.");
            return;
        }
        for (Room room : ROOMS) {
            room.displayInfo();
        }
    }

    private static void searchGuest() {
        if (GUESTS.isEmpty()) {
            System.out.println("No guests.");
            return;
        }
        String citizenId = readNonEmpty("Citizen ID to search: ");
        Guest guest = findGuest(citizenId);
        if (guest == null) {
            System.out.println("No guest found.");
            return;
        }
        guest.displayInfo();
        for (Room room : ROOMS) {
            if (room.getGuest().getCitizenId().equalsIgnoreCase(guest.getCitizenId())) {
                room.displayInfo();
                return;
            }
        }
        System.out.println("This guest has not rented a room.");
    }

    private static void showTotalRevenue() {
        if (ROOMS.isEmpty()) {
            System.out.println("No rooms rented.");
            return;
        }
        double total = 0;
        for (Room room : ROOMS) {
            total += room.calculateCost();
        }
        System.out.println("Total rental revenue: " + money(total) + " VND");
    }

    private static Guest findGuest(String citizenId) {
        for (Guest guest : GUESTS) {
            if (guest.getCitizenId().equalsIgnoreCase(citizenId.trim())) {
                return guest;
            }
        }
        return null;
    }

    private static String readRoomType() {
        while (true) {
            String value = readNonEmpty("Room type (VIP1/VIP2/VIP3): ").toUpperCase(Locale.US);
            if (value.equals("VIP1") || value.equals("VIP2") || value.equals("VIP3")) {
                return value;
            }
            System.out.println("Room type must be VIP1, VIP2 or VIP3.");
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
