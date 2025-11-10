package controller;

import model.entity.Room;
import model.enums.RoomStatus;
import service.HotelAdmin;

import java.util.List;
import java.util.Scanner;

public class RoomController extends BaseController {
    private HotelAdmin hotelAdmin;

    public RoomController(Scanner scanner, HotelAdmin hotelAdmin) {
        super(scanner);
        this.hotelAdmin = hotelAdmin;
    }

    public void showMenu() {
        String[] options = {
                "Add room",
                "Find room by number",
                "Show all rooms",
                "Update room data",
                "Delete room",
                "Find available rooms",
                "Change room status"
        };

        boolean running = true;
        while (running) {
            printMenu("Room Management", options);
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addRoom();
                    break;
                case 2:
                    findRoomByNumber();
                    break;
                case 3:
                    getAllRooms();
                    break;
                case 4:
                    updateRoom();
                    break;
                case 5:
                    deleteRoom();
                    break;
                case 6:
                    findAvailableRooms();
                    break;
                case 7:
                    changeRoomStatus();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void addRoom() {
        System.out.println("\n--- Add Room ---");
        String number = getStringInput("Enter room number: ");
        String type = getStringInput("Enter room type: ");
        double price = getDoubleInput("Enter price: ");
        int capacity = getIntInput("Enter capacity: ");
        int stars = getIntInput("Enter stars (1-5): ");

        Room room = new Room(number, type, price, capacity, stars);
        boolean success = hotelAdmin.addRoom(room);
        if (success) {
            System.out.println("Room added successfully!");
        } else {
            System.out.println("Failed to add room!");
        }
    }

    private void findRoomByNumber() {
        System.out.println("\n--- Find Room by Number ---");
        String number = getStringInput("Enter room number: ");

        Room foundRoom = hotelAdmin.getRoomManager().getRoom(number);
        if (foundRoom != null) {
            System.out.println("Found room: " + foundRoom);
        } else {
            System.out.println("Room not found!");
        }
    }

    private void getAllRooms() {
        System.out.println("\n--- All Rooms ---");
        List<Room> rooms = hotelAdmin.getRoomManager().getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("Room list is empty!");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private void updateRoom() {
        System.out.println("\n--- Update Room ---");
        String number = getStringInput("Enter room number to update: ");

        Room roomToUpdate = hotelAdmin.getRoomManager().getRoom(number);
        if (roomToUpdate != null) {
            String type = getStringInput("Enter new type: ");
            double price = getDoubleInput("Enter new price: ");
            int capacity = getIntInput("Enter new capacity: ");
            int stars = getIntInput("Enter new stars (1-5): ");

            roomToUpdate.setType(type);
            roomToUpdate.setPrice(price);
            roomToUpdate.setCapacity(capacity);
            roomToUpdate.setStars(stars);

            System.out.println("Room data updated!");
        } else {
            System.out.println("Room not found!");
        }
    }

    private void deleteRoom() {
        System.out.println("\n--- Delete Room ---");
        String number = getStringInput("Enter room number to delete: ");

        Room roomToDelete = hotelAdmin.getRoomManager().getRoom(number);
        if (roomToDelete != null) {
            System.out.println("Delete functionality needs to be implemented in RoomManager");
        } else {
            System.out.println("Room not found!");
        }
    }

    private void findAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        List<Room> allRooms = hotelAdmin.getRoomManager().getAllRooms();
        List<Room> availableRooms = allRooms.stream()
                .filter(Room::isAvailable)
                .toList();

        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms!");
        } else {
            availableRooms.forEach(System.out::println);
        }
    }

    private void changeRoomStatus() {
        System.out.println("\n--- Change Room Status ---");
        String number = getStringInput("Enter room number: ");

        System.out.println("Select new status:");
        System.out.println("1. AVAILABLE");
        System.out.println("2. OCCUPIED");
        System.out.println("3. UNDER_MAINTENANCE");
        System.out.println("4. UNDER_SERVICE");

        int statusChoice = getIntInput("Choose status: ");
        RoomStatus newStatus;

        switch (statusChoice) {
            case 1:
                newStatus = RoomStatus.AVAILABLE;
                break;
            case 2:
                newStatus = RoomStatus.OCCUPIED;
                break;
            case 3:
                newStatus = RoomStatus.UNDER_MAINTENANCE;
                break;
            case 4:
                newStatus = RoomStatus.UNDER_SERVICE;
                break;
            default:
                System.out.println("Invalid status choice!");
                return;
        }

        boolean success = hotelAdmin.changeRoomStatus(number, newStatus);
        if (success) {
            System.out.println("Room status changed successfully to: " + newStatus.getDescription());
        } else {
            System.out.println("Failed to change room status!");
        }
    }
}