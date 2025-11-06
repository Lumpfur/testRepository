package test;

import controller.*;
import model.manager.*;
import service.*;
import UI.*;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Hotel Management System");
            System.out.println("Init system...");

            // Initialize managers
            GuestManager guestManager = new GuestManager();
            RoomManager roomManager = new RoomManager();
            ServiceManager serviceManager = new ServiceManager();

            // Initialize admin service
            HotelAdmin admin = new HotelAdmin();

            // Create menu actions
            MenuAction guestMenuAction = () -> showGuestMenu(admin);
            MenuAction roomMenuAction = () -> showRoomMenu(admin);
            MenuAction serviceMenuAction = () -> showServiceMenu(admin);
            MenuAction reportMenuAction = () -> showReports(admin);
            MenuAction exitAction = () -> System.exit(0);

            // Build main menu
            Menu mainMenu = MenuBuilder.buildMainMenu(
                    guestMenuAction,
                    roomMenuAction,
                    serviceMenuAction,
                    reportMenuAction,
                    exitAction
            );

            // Start navigation
            MenuNavigator navigator = new MenuNavigator(mainMenu);
            navigator.navigate();

        } catch (Exception e) {
            System.out.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showGuestMenu(HotelAdmin admin) {
        System.out.println("\n--- Guest Management ---");
        // Add guest management logic here
        admin.displayAllGuests(); // example
    }

    private static void showRoomMenu(HotelAdmin admin) {
        System.out.println("\n--- Room Management ---");
        // Add room management logic here
        admin.displayAllRooms(); // example
    }

    private static void showServiceMenu(HotelAdmin admin) {
        System.out.println("\n--- Service Management ---");
        // Add service management logic here
        admin.displayAllServices(); // example
    }

    private static void showReports(HotelAdmin admin) {
        System.out.println("\n--- Reports ---");
        // Add reports logic here
        admin.displayTotalGuests(); // example
    }
}