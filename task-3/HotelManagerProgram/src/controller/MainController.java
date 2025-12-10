package controller;

import config.ConfigurationManager;
import model.enums.MenuType;
import service.HotelAdmin;
import config.HotelConfig;

import java.time.LocalDate;
import java.util.Scanner;

public class MainController {
    private final Scanner scanner;
    private final HotelAdmin hotelAdmin;
    private final GuestController guestController;
    private final RoomController roomController;
    private final ServiceController serviceController;

    public MainController() {
        this.scanner = new Scanner(System.in);
        this.hotelAdmin = new HotelAdmin();
        this.guestController = new GuestController(scanner, hotelAdmin);
        this.roomController = new RoomController(scanner, hotelAdmin);
        this.serviceController = new ServiceController(scanner, hotelAdmin);

        // Add shutdown hook for automatic saving when program closes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nAutomatic program state saving...");
            hotelAdmin.saveState();
        }));
    }

    public void start() {
        System.out.println("=== Welcome to Hotel Management System ===");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    guestController.showMenu();
                    break;
                case 2:
                    roomController.showMenu();
                    break;
                case 3:
                    serviceController.showMenu();
                    break;
                case 4:
                    showReportMenu();
                    break;
                case 0:
                    running = false;
                    // Save state on normal exit
                    System.out.println("Saving program state...");
                    hotelAdmin.saveState();
                    System.out.println("Thank you for using Hotel Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println("\n=== " + MenuType.MAIN.getDisplayName() + " ===");
        System.out.println("1. " + MenuType.GUEST.getDisplayName());
        System.out.println("2. " + MenuType.ROOM.getDisplayName());
        System.out.println("3. " + MenuType.SERVICE.getDisplayName());
        System.out.println("4. " + MenuType.REPORT.getDisplayName());
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
    }

    private void showReportMenu() {
        String[] options = {
                "Display all rooms (sorted)",
                "Display all guests (sorted)",
                "Display all services",
                "Display available rooms (sorted)",
                "Display regular guests (sorted)",
                "Display room details",
                "Display last 3 guests of room",
                "Display guest payment",
                "Display guest services",
                "Display prices",
                "Display total available rooms",
                "Display total guests",
                "Display rooms available by date",
                "Import guests from CSV",
                "Import rooms from CSV",
                "Import services from CSV",
                "Export guests to CSV",
                "Export rooms to CSV",
                "Export services to CSV",
                "Manage configuration",
                "Save program state",  // New option
                "Reload program state" // New option
        };

        boolean running = true;
        while (running) {
            printMenu(MenuType.REPORT.getDisplayName(), options);
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    showSortedRoomsMenu();
                    break;
                case 2:
                    showSortedGuestsMenu();
                    break;
                case 3:
                    hotelAdmin.displayAllServices();
                    break;
                case 4:
                    showSortedAvailableRoomsMenu();
                    break;
                case 5:
                    showSortedRegularGuestsMenu();
                    break;
                case 6:
                    showRoomDetails();
                    break;
                case 7:
                    showLastThreeGuests();
                    break;
                case 8:
                    showGuestPayment();
                    break;
                case 9:
                    showGuestServices();
                    break;
                case 10:
                    showPricesMenu();
                    break;
                case 11:
                    hotelAdmin.displayTotalAvailableRooms();
                    break;
                case 12:
                    hotelAdmin.displayTotalGuests();
                    break;
                case 13:
                    showRoomsAvailableByDate();
                    break;
                case 14:
                    importGuestsFromCSV();
                    break;
                case 15:
                    importRoomsFromCSV();
                    break;
                case 16:
                    importServicesFromCSV();
                    break;
                case 17:
                    exportGuestsToCSV();
                    break;
                case 18:
                    exportRoomsToCSV();
                    break;
                case 19:
                    exportServicesToCSV();
                    break;
                case 20:
                    manageConfiguration();
                    break;
                case 21:
                    // Save state
                    hotelAdmin.saveState();
                    break;
                case 22:
                    // Reload state
                    System.out.println("Reloading state...");
                    System.out.println("For full reload, please restart the program");
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void manageConfiguration() {
        System.out.println("\n=== Configuration Management ===");

        // Getting the config manager
        ConfigurationManager configManager = ConfigurationManager.getInstance();

        System.out.println("Options:");
        System.out.println("1. View all configurations");
        System.out.println("2. Reload all configurations");
        System.out.println("3. Save all configurations");
        System.out.println("4. Edit specific configuration");
        System.out.println("0. Back");

        int choice = getIntInput("Choose option: ");

        switch (choice) {
            case 1:
                configManager.printAllConfigurations();
                break;
            case 2:
                configManager.reloadAllConfigurations();
                break;
            case 3:
                configManager.saveAllConfigurations();
                break;
            case 4:
                editConfiguration();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option");
        }
    }

    private void editConfiguration() {
        System.out.println("\n=== Edit Configuration ===");
        System.out.println("Select configuration to edit:");
        System.out.println("1. Hotel Configuration");
        System.out.println("2. Room Configuration");
        System.out.println("3. Service Configuration");
        System.out.println("0. Back");

        int choice = getIntInput("Choose option: ");

        switch (choice) {
            case 1:
                editHotelConfig();
                break;
            case 2:
                editRoomConfig();
                break;
            case 3:
                editServiceConfig();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option");
        }
    }

    private void editHotelConfig() {
        HotelConfig config = HotelConfig.getInstance();
        System.out.println("\n=== Edit Hotel Configuration ===");

        // Display current values
        System.out.println("Current configuration:");
        System.out.println("Room status change enabled: " + config.isRoomStatusChangeEnabled());
        System.out.println("Room history size: " + config.getRoomHistorySize());

        // Ask for new values
        System.out.print("Enable room status change? (true/false): ");
        String statusInput = scanner.nextLine();
        boolean roomStatusChangeEnabled = Boolean.parseBoolean(statusInput);

        System.out.print("Enter room history size: ");
        int historySize = getIntInput();

        // Update configuration
        config.updateConfiguration(roomStatusChangeEnabled, historySize);

        System.out.println("Hotel configuration updated");
    }

    private void editRoomConfig() {
        System.out.println("\n=== Edit Room Configuration ===");
        System.out.println("Room configuration editing not implemented yet.");
        // TODO: Implement room configuration editing
    }

    private void editServiceConfig() {
        System.out.println("\n=== Edit Service Configuration ===");
        System.out.println("Service configuration editing not implemented yet.");
        // TODO: Implement service configuration editing
    }

    // Helper method to get boolean input
    private boolean getBooleanInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().toLowerCase();
        return input.equals("y") || input.equals("yes") || input.equals("true") || input.equals("1");
    }

    private void showSortedRoomsMenu() {
        System.out.println("\n--- Sort Rooms By ---");
        System.out.println("1. Number");
        System.out.println("2. Type");
        System.out.println("3. Price");
        System.out.println("4. Capacity");
        System.out.println("5. Stars");
        System.out.print("Choose sort option: ");

        int sortChoice = getIntInput();
        String sortBy = switch (sortChoice) {
            case 1 -> "number";
            case 2 -> "type";
            case 3 -> "price";
            case 4 -> "capacity";
            case 5 -> "stars";
            default -> "number";
        };

        hotelAdmin.displayAllRoomsSorted(sortBy);
    }

    private void showSortedGuestsMenu() {
        System.out.println("\n--- Sort Guests By ---");
        System.out.println("1. Name");
        System.out.println("2. ID");
        System.out.println("3. Email");
        System.out.print("Choose sort option: ");

        int sortChoice = getIntInput();
        String sortBy = switch (sortChoice) {
            case 1 -> "name";
            case 2 -> "id";
            case 3 -> "email";
            default -> "name";
        };

        hotelAdmin.displayRegularGuestsSorted(sortBy);
    }

    private void showSortedAvailableRoomsMenu() {
        System.out.println("\n--- Sort Available Rooms By ---");
        System.out.println("1. Number");
        System.out.println("2. Type");
        System.out.println("3. Price");
        System.out.println("4. Capacity");
        System.out.print("Choose sort option: ");

        int sortChoice = getIntInput();
        String sortBy = switch (sortChoice) {
            case 1 -> "number";
            case 2 -> "type";
            case 3 -> "price";
            case 4 -> "capacity";
            default -> "number";
        };

        hotelAdmin.displayAvailableRoomsSorted(sortBy);
    }

    private void showSortedRegularGuestsMenu() {
        System.out.println("\n--- Sort Regular Guests By ---");
        System.out.println("1. Name");
        System.out.println("2. Stay count");
        System.out.println("3. Last stay date");
        System.out.print("Choose sort option: ");

        int sortChoice = getIntInput();
        String sortBy = switch (sortChoice) {
            case 1 -> "name";
            case 2 -> "stayCount";
            case 3 -> "lastStay";
            default -> "name";
        };

        hotelAdmin.displayRegularGuestsSorted(sortBy);
    }

    private void showRoomDetails() {
        String roomNumber = getStringInput("Enter room number: ");
        hotelAdmin.displayRoomDetails(roomNumber);
    }

    private void showLastThreeGuests() {
        String roomNumber = getStringInput("Enter room number: ");
        hotelAdmin.displayLastThreeGuests(roomNumber);
    }

    private void showGuestPayment() {
        String roomNumber = getStringInput("Enter room number: ");
        hotelAdmin.displayGuestPayment(roomNumber);
    }

    private void showGuestServices() {
        String roomNumber = getStringInput("Enter room number: ");
        System.out.println("Sort services by:");
        System.out.println("1. Name");
        System.out.println("2. Price");
        int sortChoice = getIntInput("Choose sort option: ");
        String sortBy = (sortChoice == 2) ? "price" : "name";
        hotelAdmin.displayGuestServices(roomNumber, sortBy);
    }

    private void showPricesMenu() {
        System.out.println("Display prices for:");
        System.out.println("1. Rooms");
        System.out.println("2. Services");
        int categoryChoice = getIntInput("Choose category: ");
        String category = (categoryChoice == 2) ? "services" : "rooms";

        System.out.println("Sort by:");
        System.out.println("1. Name/Type");
        System.out.println("2. Price");
        int sortChoice = getIntInput("Choose sort option: ");
        String sortBy = (sortChoice == 2) ? "price" : "name";

        hotelAdmin.displayPrices(category, sortBy);
    }

    private void showRoomsAvailableByDate() {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateInput = scanner.nextLine();

        try {
            LocalDate date = LocalDate.parse(dateInput);
            hotelAdmin.displayRoomsAvailableByDate(date);
        } catch (Exception e) {
            System.out.println("Invalid date format! Please use YYYY-MM-DD format.");
        }
    }

    // Helper methods for data input
    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        return getIntInput();
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private void printMenu(String title, String[] options) {
        System.out.println("\n=== " + title + " ===");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Back");
        System.out.print("Choose option: ");
    }

    private void importGuestsFromCSV() {
        try {
            String filePath = getStringInput("Enter CSV file path: ");
            hotelAdmin.importGuestsFromCSV(filePath);
            System.out.println("Guests imported successfully!");
        } catch (Exception e) {
            System.out.println("Import failed: " + e.getMessage());
        }
    }

    private void importRoomsFromCSV() {
        try {
            String filePath = getStringInput("Enter CSV file path: ");
            hotelAdmin.importRoomsFromCSV(filePath);
            System.out.println("Rooms imported successfully!");
        } catch (Exception e) {
            System.out.println("Import failed: " + e.getMessage());
        }
    }

    private void importServicesFromCSV() {
        try {
            String filePath = getStringInput("Enter CSV file path: ");
            hotelAdmin.importServicesFromCSV(filePath);
            System.out.println("Services imported successfully!");
        } catch (Exception e) {
            System.out.println("Import failed: " + e.getMessage());
        }
    }

    private void exportGuestsToCSV() {
        try {
            String filePath = getStringInput("Enter CSV file path: ");
            hotelAdmin.exportGuestsToCSV(filePath);
            System.out.println("Guests exported successfully!");
        } catch (Exception e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }

    private void exportRoomsToCSV() {
        try {
            String filePath = getStringInput("Enter CSV file path: ");
            hotelAdmin.exportRoomsToCSV(filePath);
            System.out.println("Rooms exported successfully!");
        } catch (Exception e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }

    private void exportServicesToCSV() {
        try {
            String filePath = getStringInput("Enter CSV file path: ");
            hotelAdmin.exportServicesToCSV(filePath);
            System.out.println("Services exported successfully!");
        } catch (Exception e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }
}