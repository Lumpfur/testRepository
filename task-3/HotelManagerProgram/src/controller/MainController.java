package controller;

import model.enums.MenuType;
import service.HotelAdmin;

import java.util.Scanner;

public class MainController {
    private Scanner scanner;
    private HotelAdmin hotelAdmin;
    private GuestController guestController;
    private RoomController roomController;
    private ServiceController serviceController;

    public MainController() {
        this.scanner = new Scanner(System.in);
        this.hotelAdmin = new HotelAdmin();
        this.guestController = new GuestController(scanner, hotelAdmin);
        this.roomController = new RoomController(scanner, hotelAdmin);
        this.serviceController = new ServiceController(scanner, hotelAdmin);
    }

    public void start() {
        System.out.println("Welcome to Hotel Management System!");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    System.out.println("\n=== " + MenuType.GUEST.getDisplayName() + " ===");
                    guestController.showMenu();
                    break;
                case 2:
                    System.out.println("\n=== " + MenuType.ROOM.getDisplayName() + " ===");
                    roomController.showMenu();
                    break;
                case 3:
                    System.out.println("\n=== " + MenuType.SERVICE.getDisplayName() + " ===");
                    serviceController.showMenu();
                    break;
                case 4:
                    System.out.println("\n=== " + MenuType.REPORT.getDisplayName() + " ===");
                    showReportMenu();
                    break;
                case 0:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option!");
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
                "Display all rooms",
                "Display all guests",
                "Display all services",
                "Display available rooms",
                "Display regular guests",
                "Display room details"
        };

        boolean running = true;
        while (running) {
            printMenu(MenuType.REPORT.getDisplayName(), options);
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    hotelAdmin.displayAllRooms();
                    break;
                case 2:
                    hotelAdmin.displayAllGuests();
                    break;
                case 3:
                    hotelAdmin.displayAllServices();
                    break;
                case 4:
                    hotelAdmin.displayAvailableRoomsSorted("number");
                    break;
                case 5:
                    hotelAdmin.displayRegularGuestsSorted("name");
                    break;
                case 6:
                    String roomNumber = getStringInput("Enter room number: ");
                    hotelAdmin.displayRoomDetails(roomNumber);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
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