package task4;

public class HotelTest {
    public static void main(String[] args) {
        HotelAdmin admin = new HotelAdmin();
        try {
            System.out.println("init");

            admin.addRoom(new Room("101", "Standard", 2500.0));

            admin.addGuest(new Guest("G001", "John", "+79161234567", "john@mail.ru"));

            admin.checkIn("101", "G001");

            admin.displayAllRooms();

            admin.checkOut("101");

            //final statistic
            //admin.displayAllRooms();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        finally {
            System.out.println("end");
        }
    }
}
