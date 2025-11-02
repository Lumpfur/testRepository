package task4;
import java.time.LocalDate;

public class HotelTest {
    public static void main(String[] args) {
        HotelAdmin admin = new HotelAdmin();

        try {
            System.out.println("init");

            admin.addRoom(new Room("101", "Standard", 2500.0, 2, 3));
            admin.addRoom(new Room("102", "Standard", 2700.0, 2, 3));
            admin.addRoom(new Room("201", "Lux", 5000.0, 3, 4));
            admin.addRoom(new Room("301", "Suite", 8000.0, 4, 5));

            admin.addGuest(new Guest("G001", "John Smith", "+79161234567", "john@mail.com"));
            admin.addGuest(new Guest("G002", "Jane Doe", "+79161234568", "jane@mail.com"));
            admin.addService(new Service("S001", "Breakfast", 500.0, "Buffet breakfast"));
            admin.addService(new Service("S002", "SPA", 1500.0, "Spa procedures"));

            admin.checkIn("101", "G001");
            admin.checkIn("201", "G002");

            admin.addServiceToGuest("101", "S001");
            admin.addServiceToGuest("201", "S002");

            // Demonstrate new features
            admin.displayAllRoomsSorted("price");
            admin.displayAllRoomsSorted("stars");
            admin.displayAvailableRoomsSorted("capacity");

            admin.displayGuestPayment("101");
            admin.displayGuestServices("101", "price");

            admin.displayRoomDetails("101");

            // Rooms available by date
            admin.displayRoomsAvailableByDate(LocalDate.now().plusDays(3));

            // Total statistics
            admin.displayTotalAvailableRooms();
            admin.displayTotalGuests();

            // Check out guests to demonstrate history
            admin.checkOut("101");
            admin.checkOut("201");

            // Stay history
            admin.displayLastThreeGuests("101");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("end");
        }
    }
}

