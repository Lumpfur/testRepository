package task4;

public class HotelTest {
    public static void main(String[] args) {
        HotelAdmin admin = new HotelAdmin();
        try {
            System.out.println("init");

            admin.addRoom(new Room("101", "Standard", 2500.0));
            admin.addRoom(new Room("104", "Lux", 3000.0));

            admin.addGuest(new Guest("G001", "John", "+79161234567", "john@mail.ru"));
            admin.addService(new Service("S001", "Завтрак", 500.0, "Шведский стол"));

            admin.checkIn("101", "G001");

            admin.displayAllGuests();
            admin.displayAllServices();

            admin.checkOut("101");

            admin.changeRoomStatus("104", RoomStatus.UNDER_MAINTENANCE);
            admin.changeRoomPrice("104", 3500.0);

            admin.displayAllRooms();

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
