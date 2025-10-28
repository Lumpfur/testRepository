package task4;

public class HotelAdmin {
    private RoomManager roomManager;
    private GuestManager guestManager;
    private ServiceManager serviceManager;

    public HotelAdmin() {
        this.roomManager = new RoomManager();
        this.guestManager = new GuestManager();
        this.serviceManager = new ServiceManager();
    }

    public boolean checkIn(String roomNumber, String guestId) {
        Guest guest = guestManager.getGuest(guestId);
        if (guest == null) {
            System.out.println("Error: id " + guestId + " was not found");
            return false;
        }

        boolean success = roomManager.checkInGuest(roomNumber, guest);
        if (success) {
            System.out.println("Guest " + guest.getName() + " has checked in " + roomNumber);
        } else {
            System.out.println("Error: U cant check in the guest " + roomNumber);
        }
        return success;
    }

    public boolean checkOut(String roomNumber) {
        boolean success = roomManager.checkOutGuest(roomNumber);
        if (success) {
            System.out.println("The guest has checked out from the room " + roomNumber);
        } else {
            System.out.println("Error: U cant check out the guest " + roomNumber);
        }
        return success;
    }

    public boolean changeRoomStatus(String roomNumber, RoomStatus newStatus) {
        boolean success = roomManager.changeRoomStatus(roomNumber, newStatus);
        if (success) {
            System.out.println("Room status " + roomNumber + " has changed: " + newStatus.getDescription());
        } else {
            System.out.println("Error: U cant change the room status " + roomNumber);
        }
        return success;
    }

    public boolean changeRoomPrice(String roomNumber, double newPrice) {
        boolean success = roomManager.changeRoomPrice(roomNumber, newPrice);
        if (success) {
            System.out.println("Цена номера " + roomNumber + " изменена на: " + newPrice + " руб.");
        } else {
            System.out.println("Ошибка: Не удалось изменить цену номера " + roomNumber);
        }
        return success;
    }

    public boolean changeServicePrice(String serviceId, double newPrice) {
        boolean success = serviceManager.changeServicePrice(serviceId, newPrice);
        if (success) {
            System.out.println("Price for the service " + serviceId + " has changed to: " + newPrice + " руб.");
        } else {
            System.out.println("Error: U cant change the price for the service " + serviceId);
        }
        return success;
    }

    public boolean addRoom(Room room) {
        boolean success = roomManager.addRoom(room);
        if (success) {
            System.out.println("Room " + room.getNumber() + " has added");
        } else {
            System.out.println("Error: u cant add the room " + room.getNumber());
        }
        return success;
    }

    public boolean addService(Service service) {
        boolean success = serviceManager.addService(service);
        if (success) {
            System.out.println("Service " + service.getName() + " has added");
        } else {
            System.out.println("Error: U cant add the service " + service.getName());
        }
        return success;
    }

    public boolean addGuest(Guest guest) {
        boolean success = guestManager.addGuest(guest);
        if (success) {
            System.out.println("Guest " + guest.getName() + " has added");
        } else {
            System.out.println("Error: U cant add the guest " + guest.getName());
        }
        return success;
    }

    public void displayAllRooms() {
        System.out.println("\n=== All Rooms ===");
        roomManager.getAllRooms().forEach(System.out::println);
    }

    public void displayAllGuests() {
        System.out.println("\n=== All Guests ===");
        guestManager.getAllGuests().forEach(System.out::println);
    }

    public void displayAllServices() {
        System.out.println("\n=== All Services ===");
        serviceManager.getAllServices().forEach(System.out::println);
    }


    public RoomManager getRoomManager() {
        return roomManager;
    }
    public GuestManager getGuestManager() {
        return guestManager;
    }
    public ServiceManager getServiceManager() {
        return serviceManager;
    }


}
