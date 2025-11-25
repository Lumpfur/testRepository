package test;

import model.entity.Guest;
import model.entity.Room;
import model.entity.Service;
import service.HotelAdmin;

import java.time.LocalDate;

public class HotelTest {
    public static void main(String[] args) {
        HotelAdmin admin = new HotelAdmin();

        try {
            System.out.println("Init hotel system...");

            admin.addRoom(new Room("101", "Standard", 2500.0, 2, 3));
            admin.addRoom(new Room("102", "Standard", 2700.0, 2, 3));
            admin.addRoom(new Room("201", "Lux", 5000.0, 3, 4));
            admin.addRoom(new Room("301", "Suite", 8000.0, 4, 5));

            admin.addGuest(new Guest("G001", "John Smith", "+79161234567", "john@mail.com"));
            admin.addGuest(new Guest("G002", "Jane Doe", "+79161234568", "jane@mail.com"));
            admin.addGuest(new Guest("G003", "Bob Johnson", "+79161234569", "bob@mail.com"));
            admin.addGuest(new Guest("G004", "Alice Brown", "+79161234570", "alice@mail.com"));

            admin.addService(new Service("S001", "Breakfast", 500.0, "Buffet breakfast"));
            admin.addService(new Service("S002", "SPA", 1500.0, "Spa procedures"));

            // Demo: Multiple guests in one room
            System.out.println("\n--- Testing multiple guests per room ---");
            admin.checkInGuests("201", "G001", "G002"); // Two guests in one room
            admin.checkIn("301", "G003"); // Single guest in large room

            admin.addGuestToRoom("301", "G004");

            admin.addServiceToGuest("201", "S001");
            admin.addServiceToGuest("301", "S002");

            // Demonstrate new features
            admin.displayAllRoomsSorted("price");
            admin.displayAllRoomsSorted("stars");
            admin.displayAvailableRoomsSorted("capacity");

            // Show current guests count
            admin.displayTotalGuests(); // Now shows actual current guests count

            admin.displayGuestPayment("201");
            admin.displayGuestServices("201", "price");

            admin.displayRoomDetails("201");
            admin.displayRoomDetails("301");

            // Rooms available by date
            admin.displayRoomsAvailableByDate(LocalDate.now().plusDays(3));

            // Total statistics
            admin.displayTotalAvailableRooms();
            admin.displayTotalGuests(); // This now shows current guests, not historical

            // Check out guests to demonstrate history
            admin.checkOut("201");
            admin.checkOut("301");

            // Stay history
            admin.displayLastThreeGuests("201");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("end");
        }
    }
}