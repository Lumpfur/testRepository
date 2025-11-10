package controller;

import model.enums.MenuType;
import service.HotelAdmin;

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
                "Display last 3 guests of room"
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
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
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

    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
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
}