package service;

import model.entity.*;
import model.enums.*;
import model.manager.*;
import exception.ImportException;
import exception.ExportException;
import config.HotelConfig;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class HotelAdmin implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String SAVE_FILE = "hotel_state.ser";

    private final RoomManager roomManager;
    private final GuestManager guestManager;
    private final ServiceManager serviceManager;
    private final HotelReport hotelReport;

    public HotelAdmin() {
        HotelAdmin loaded = loadState();
        if (loaded != null) {
            this.roomManager = loaded.roomManager;
            this.guestManager = loaded.guestManager;
            this.serviceManager = loaded.serviceManager;
            this.hotelReport = new HotelReport(roomManager, guestManager);
            System.out.println("Program status was loaded from file");
        } else {
            this.roomManager = new RoomManager();
            this.guestManager = new GuestManager();
            this.serviceManager = new ServiceManager();
            this.hotelReport = new HotelReport(roomManager, guestManager);
            System.out.println("The new data base of hotel was created");
        }
    }

    // method for saving statement
    public void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(this);
            System.out.println("Program status was saved into the file: " + SAVE_FILE);
        } catch (IOException e) {
            System.err.println("Error: saving into the file: " + e.getMessage());
        }
    }

    //method for loading statement
    private HotelAdmin loadState() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("There is no loading file, creating new data base");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SAVE_FILE))) {
            HotelAdmin loaded = (HotelAdmin) ois.readObject();
            System.out.println("Statement loading success");
            return loaded;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: loading statement: " + e.getMessage());
            return null;
        }
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

    public boolean checkInGuests(String roomNumber, String... guestIds) {
        List<Guest> guests = Arrays.stream(guestIds)
                .map(guestManager::getGuest)
                .filter(guest -> guest != null)
                .collect(java.util.stream.Collectors.toList());

        if (guests.size() != guestIds.length) {
            System.out.println("Error: Some guest IDs not found");
            return false;
        }

        boolean success = roomManager.checkInGuests(roomNumber, guests);
        if (success) {
            System.out.println("Guests " + guests.stream().map(Guest::getName).collect(java.util.stream.Collectors.joining(", ")) +
                    " checked into room " + roomNumber);
        } else {
            System.out.println("Error: Cannot check in guests to room " + roomNumber);
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
        if (!HotelConfig.getInstance().isRoomStatusChangeEnabled()) {
            System.out.println("Error: Room status changes are currently disabled in system configuration");
            return false;
        }

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

    public boolean addGuestToRoom(String roomNumber, String guestId) {
        Guest guest = guestManager.getGuest(guestId);
        if (guest == null) {
            System.out.println("Error: Guest ID " + guestId + " not found");
            return false;
        }

        boolean success = roomManager.addGuestToRoom(roomNumber, guest);
        if (success) {
            System.out.println("Guest " + guest.getName() + " added to room " + roomNumber);
        } else {
            System.out.println("Error: Cannot add guest to room " + roomNumber);
        }
        return success;
    }

    public boolean removeGuestFromRoom(String roomNumber, String guestId) {
        Guest guest = guestManager.getGuest(guestId);
        if (guest == null) {
            System.out.println("Error: Guest ID " + guestId + " not found");
            return false;
        }

        boolean success = roomManager.removeGuestFromRoom(roomNumber, guest);
        if (success) {
            System.out.println("Guest " + guest.getName() + " removed from room " + roomNumber);
        } else {
            System.out.println("Error: Cannot remove guest from room " + roomNumber);
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

    public void importGuestsFromCSV(String filePath) {
        try {
            List<Guest> importedGuests = CSVImporter.importGuests(filePath, this);
            System.out.println("Successfully imported " + importedGuests.size() + " guests");
        } catch (ImportException e) {
            System.out.println("Import failed: " + e.getMessage());
            throw e;
        }
    }

    public void importRoomsFromCSV(String filePath) {
        try {
            List<Room> importedRooms = CSVImporter.importRooms(filePath, this);
            System.out.println("Successfully imported " + importedRooms.size() + " rooms");
        } catch (ImportException e) {
            System.out.println("Import failed: " + e.getMessage());
            throw e;
        }
    }

    public void importServicesFromCSV(String filePath) {
        try {
            List<Service> importedServices = CSVImporter.importServices(filePath, this);
            System.out.println("Successfully imported " + importedServices.size() + " services");
        } catch (ImportException e) {
            System.out.println("Import failed: " + e.getMessage());
            throw e;
        }
    }

    public void exportGuestsToCSV(String filePath) {
        try {
            List<Guest> guests = guestManager.getAllGuests();
            CSVExporter.exportGuests(guests, filePath);
            System.out.println("Successfully exported " + guests.size() + " guests to " + filePath);
        } catch (ExportException e) {
            System.out.println("Export failed: " + e.getMessage());
            throw e;
        }
    }

    public void exportRoomsToCSV(String filePath) {
        try {
            List<Room> rooms = roomManager.getAllRooms();
            CSVExporter.exportRooms(rooms, filePath);
            System.out.println("Successfully exported " + rooms.size() + " rooms to " + filePath);
        } catch (ExportException e) {
            System.out.println("Export failed: " + e.getMessage());
            throw e;
        }
    }

    public void exportServicesToCSV(String filePath) {
        try {
            List<Service> services = serviceManager.getAllServices();
            CSVExporter.exportServices(services, filePath);
            System.out.println("Successfully exported " + services.size() + " services to " + filePath);
        } catch (ExportException e) {
            System.out.println("Export failed: " + e.getMessage());
            throw e;
        }
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

    public void reloadConfiguration() {
        HotelConfig.getInstance().reloadConfiguration();
        System.out.println("Configuration reloaded from file");
    }

    public RoomManager getRoomManager() { return roomManager; }
    public GuestManager getGuestManager() { return guestManager; }
    public ServiceManager getServiceManager() { return serviceManager; }
}