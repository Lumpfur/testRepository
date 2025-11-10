package controller;

import model.entity.Guest;
import service.HotelAdmin;

import java.util.List;
import java.util.Scanner;

public class GuestController extends BaseController {
    private final HotelAdmin hotelAdmin;

    public GuestController(Scanner scanner, HotelAdmin hotelAdmin) {
        super(scanner);
        this.hotelAdmin = hotelAdmin;
    }

    public void showMenu() {
        String[] options = {
                "Add guest",
                "Find guest by ID",
                "Show all guests",
                "Update guest data"
        };

        boolean running = true;
        while (running) {
            printMenu("Guest Management", options);
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addGuest();
                    break;
                case 2:
                    findGuestById();
                    break;
                case 3:
                    getAllGuests();
                    break;
                case 4:
                    updateGuest();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void addGuest() {
        System.out.println("\n--- Add Guest ---");
        String id = getStringInput("Enter guest ID: ");
        String name = getStringInput("Enter guest name: ");
        String phone = getStringInput("Enter phone: ");
        String email = getStringInput("Enter email: ");

        Guest guest = new Guest(id, name, phone, email);
        boolean success = hotelAdmin.addGuest(guest);
        if (success) {
            System.out.println("Guest added successfully!");
        } else {
            System.out.println("Failed to add guest!");
        }
    }

    private void findGuestById() {
        System.out.println("\n--- Find Guest by ID ---");
        String id = getStringInput("Enter guest ID: ");

        Guest foundGuest = hotelAdmin.getGuestManager().getGuest(id);
        if (foundGuest != null) {
            System.out.println("Found guest: " + foundGuest);
        } else {
            System.out.println("Guest not found!");
        }
    }

    private void getAllGuests() {
        System.out.println("\n--- All Guests ---");
        List<Guest> guests = hotelAdmin.getGuestManager().getAllGuests();
        if (guests.isEmpty()) {
            System.out.println("Guest list is empty!");
        } else {
            guests.forEach(System.out::println);
        }
    }

    private void updateGuest() {
        System.out.println("\n--- Update Guest ---");
        String id = getStringInput("Enter guest ID to update: ");

        Guest guestToUpdate = hotelAdmin.getGuestManager().getGuest(id);
        if (guestToUpdate != null) {
            String name = getStringInput("Enter new name: ");
            String phone = getStringInput("Enter new phone: ");
            String email = getStringInput("Enter new email: ");

            guestToUpdate.setName(name);
            guestToUpdate.setPhone(phone);
            guestToUpdate.setEmail(email);

            System.out.println("Guest data updated!");
        } else {
            System.out.println("Guest not found!");
        }
    }
}