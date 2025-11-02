package task4;
import java.time.LocalDate;

public class HotelAdmin {
    private RoomManager roomManager;
    private GuestManager guestManager;
    private ServiceManager serviceManager;
    private HotelReport hotelReport;

    public HotelAdmin() {
        this.roomManager = new RoomManager();
        this.guestManager = new GuestManager();
        this.serviceManager = new ServiceManager();
        this.hotelReport = new HotelReport(roomManager, guestManager);
    }

    public boolean checkIn(String roomNumber, String guestId) {
        Guest guest = guestManager.getGuest(guestId);
        if (guest == null) {
            System.out.println("Error: Guest ID " + guestId + " not found");
            return false;
        }

        boolean success = roomManager.checkInGuest(roomNumber, guest);
        if (success) {
            System.out.println("Guest " + guest.getName() + " checked into room " + roomNumber);
        } else {
            System.out.println("Error: Cannot check in guest to room " + roomNumber);
        }
        return success;
    }

    public boolean checkOut(String roomNumber) {
        boolean success = roomManager.checkOutGuest(roomNumber);
        if (success) {
            System.out.println("Guest checked out from room " + roomNumber);
        } else {
            System.out.println("Error: Cannot check out from room " + roomNumber);
        }
        return success;
    }

    public boolean changeRoomStatus(String roomNumber, RoomStatus newStatus) {
        boolean success = roomManager.changeRoomStatus(roomNumber, newStatus);
        if (success) {
            System.out.println("Room " + roomNumber + " status changed to: " + newStatus.getDescription());
        } else {
            System.out.println("Error: Cannot change status for room " + roomNumber);
        }
        return success;
    }

    public boolean changeRoomPrice(String roomNumber, double newPrice) {
        boolean success = roomManager.changeRoomPrice(roomNumber, newPrice);
        if (success) {
            System.out.println("Room " + roomNumber + " price changed to: " + newPrice);
        } else {
            System.out.println("Error: Cannot change price for room " + roomNumber);
        }
        return success;
    }

    public boolean changeServicePrice(String serviceId, double newPrice) {
        boolean success = serviceManager.changeServicePrice(serviceId, newPrice);
        if (success) {
            System.out.println("Service " + serviceId + " price changed to: " + newPrice);
        } else {
            System.out.println("Error: Cannot change price for service " + serviceId);
        }
        return success;
    }

    public boolean addRoom(Room room) {
        boolean success = roomManager.addRoom(room);
        if (success) {
            System.out.println("Room " + room.getNumber() + " added");
        } else {
            System.out.println("Error: Cannot add room " + room.getNumber());
        }
        return success;
    }

    public boolean addService(Service service) {
        boolean success = serviceManager.addService(service);
        if (success) {
            System.out.println("Service " + service.getName() + " added");
        } else {
            System.out.println("Error: Cannot add service " + service.getName());
        }
        return success;
    }

    public boolean addGuest(Guest guest) {
        boolean success = guestManager.addGuest(guest);
        if (success) {
            System.out.println("Guest " + guest.getName() + " added");
        } else {
            System.out.println("Error: Cannot add guest " + guest.getName());
        }
        return success;
    }

    // Add service to guest
    public boolean addServiceToGuest(String roomNumber, String serviceId) {
        Room room = roomManager.getRoom(roomNumber);
        Service service = serviceManager.getService(serviceId);
        if (room != null && service != null) {
            boolean success = room.addService(service);
            if (success) {
                System.out.println("Service " + service.getName() + " added to guest in room " + roomNumber);
            }
            return success;
        }
        return false;
    }

    // Reporting methods
    public void displayAllRoomsSorted(String sortBy) {
        hotelReport.displayAllRoomsSorted(sortBy);
    }

    public void displayAvailableRoomsSorted(String sortBy) {
        hotelReport.displayAvailableRoomsSorted(sortBy);
    }

    public void displayRegularGuestsSorted(String sortBy) {
        hotelReport.displayRegularGuestsSorted(sortBy);
    }

    public void displayTotalAvailableRooms() {
        hotelReport.displayTotalAvailableRooms();
    }

    public void displayTotalGuests() {
        hotelReport.displayTotalGuests();
    }

    public void displayRoomsAvailableByDate(LocalDate date) {
        hotelReport.displayRoomsAvailableByDate(date);
    }

    public void displayGuestPayment(String roomNumber) {
        hotelReport.displayGuestPayment(roomNumber);
    }

    public void displayLastThreeGuests(String roomNumber) {
        hotelReport.displayLastThreeGuests(roomNumber);
    }

    public void displayGuestServices(String roomNumber, String sortBy) {
        hotelReport.displayGuestServices(roomNumber, sortBy);
    }

    public void displayPrices(String category, String sortBy) {
        hotelReport.displayPrices(category, sortBy);
    }

    public void displayRoomDetails(String roomNumber) {
        hotelReport.displayRoomDetails(roomNumber);
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

    public RoomManager getRoomManager() { return roomManager; }
    public GuestManager getGuestManager() { return guestManager; }
    public ServiceManager getServiceManager() { return serviceManager; }
}