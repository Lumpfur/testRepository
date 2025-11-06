package controller;

import model.entity.Guest;
import model.entity.Room;
import model.entity.Service;
import service.HotelAdmin;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HotelController {
    private HotelAdmin hotelAdmin;
    private Scanner scanner;
    private static HotelController instance;

    // Singleton pattern
    public static HotelController getInstance() {
        if (instance == null) {
            instance = new HotelController();
        }
        return instance;
    }

    private HotelController() {
        this.hotelAdmin = new HotelAdmin();
        this.scanner = new Scanner(System.in);
        initializeSampleData();
    }

    private void initializeSampleData() {
        //adding the test info in case there is no
        if (hotelAdmin.getRoomManager().getAllRooms().isEmpty()) {
            hotelAdmin.addRoom(new Room("101", "Standard", 2500.0, 2, 3));
            hotelAdmin.addRoom(new Room("102", "Standard", 2700.0, 2, 3));
            hotelAdmin.addRoom(new Room("201", "Lux", 5000.0, 3, 4));
            hotelAdmin.addRoom(new Room("301", "Suite", 8000.0, 4, 5));
        }

        if (hotelAdmin.getGuestManager().getAllGuests().isEmpty()) {
            hotelAdmin.addGuest(new Guest("G001", "John Smith", "+79161234567", "john@mail.com"));
            hotelAdmin.addGuest(new Guest("G002", "Jane Doe", "+79161234568", "jane@mail.com"));
            hotelAdmin.addGuest(new Guest("G003", "Bob Johnson", "+79161234569", "bob@mail.com"));
        }

        if (hotelAdmin.getServiceManager().getAllServices().isEmpty()) {
            hotelAdmin.addService(new Service("S001", "Breakfast", 500.0, "Buffet breakfast"));
            hotelAdmin.addService(new Service("S002", "SPA", 1500.0, "Spa procedures"));
        }
    }

    // Methods to work with the rooms
    public List<Room> getAllRooms() {
        return hotelAdmin.getRoomManager().getAllRooms();
    }

    public List<Room> getAvailableRooms() {
        return hotelAdmin.getRoomManager().getAllRooms().stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
    }

    public Room getRoom(String roomNumber) {
        return hotelAdmin.getRoomManager().getRoom(roomNumber);
    }

    public boolean checkIn(String roomNumber, String guestId) {
        return hotelAdmin.checkIn(roomNumber, guestId);
    }

    public boolean checkInGuests(String roomNumber, List<String> guestIds) {
        return hotelAdmin.checkInGuests(roomNumber, guestIds.toArray(new String[0]));
    }

    public boolean checkOut(String roomNumber) {
        return hotelAdmin.checkOut(roomNumber);
    }

    public boolean addRoom(Room room) {
        return hotelAdmin.addRoom(room);
    }

    // methods to work with the guests
    public List<Guest> getAllGuests() {
        return hotelAdmin.getGuestManager().getAllGuests();
    }

    public Guest getGuest(String guestId) {
        return hotelAdmin.getGuestManager().getGuest(guestId);
    }

    public boolean addGuest(Guest guest) {
        return hotelAdmin.addGuest(guest);
    }

    // Методы для работы с услугами
    public List<Service> getAllServices() {
        return hotelAdmin.getServiceManager().getAllServices();
    }

    public boolean addService(Service service) {
        return hotelAdmin.addService(service);
    }

    public boolean addServiceToGuest(String roomNumber, String serviceId) {
        return hotelAdmin.addServiceToGuest(roomNumber, serviceId);
    }

    // Методы для отчетов
    public void displayAllRoomsSorted(String sortBy) {
        hotelAdmin.displayAllRoomsSorted(sortBy);
    }

    public void displayAvailableRoomsSorted(String sortBy) {
        hotelAdmin.displayAvailableRoomsSorted(sortBy);
    }

    public void displayTotalGuests() {
        hotelAdmin.displayTotalGuests();
    }

    public void displayTotalAvailableRooms() {
        hotelAdmin.displayTotalAvailableRooms();
    }

    public void displayRoomDetails(String roomNumber) {
        hotelAdmin.displayRoomDetails(roomNumber);
    }

    public void displayGuestServices(String roomNumber, String sortBy) {
        hotelAdmin.displayGuestServices(roomNumber, sortBy);
    }

    public void displayGuestPayment(String roomNumber) {
        hotelAdmin.displayGuestPayment(roomNumber);
    }

    public void displayLastThreeGuests(String roomNumber) {
        hotelAdmin.displayLastThreeGuests(roomNumber);
    }

    public void displayRoomsAvailableByDate(LocalDate date) {
        hotelAdmin.displayRoomsAvailableByDate(date);
    }

    // methods for the UI
    public Scanner getScanner() {
        return scanner;
    }

    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }

    public HotelAdmin getHotelAdmin() {
        return hotelAdmin;
    }
}