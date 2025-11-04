package task4;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class HotelReport {
    private RoomManager roomManager;
    private GuestManager guestManager;

    public HotelReport(RoomManager roomManager, GuestManager guestManager) {
        this.roomManager = roomManager;
        this.guestManager = guestManager;
    }

    public void displayAllRoomsSorted(String sortBy) {
        List<Room> rooms = roomManager.getAllRooms();

        switch (sortBy.toLowerCase()) {
            case "price":
                rooms.sort(Comparator.comparing(Room::getPrice));
                break;
            case "capacity":
                rooms.sort(Comparator.comparing(Room::getCapacity));
                break;
            case "stars":
                rooms.sort(Comparator.comparing(Room::getStars).reversed());
                break;
            default:
                rooms.sort(Comparator.comparing(Room::getNumber));
        }

        System.out.println("\n=== All Rooms (sorted by " + sortBy + ") ===");
        rooms.forEach(System.out::println);
    }

    public void displayAvailableRoomsSorted(String sortBy) {
        List<Room> availableRooms = roomManager.getAllRooms().stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());

        switch (sortBy.toLowerCase()) {
            case "price":
                availableRooms.sort(Comparator.comparing(Room::getPrice));
                break;
            case "capacity":
                availableRooms.sort(Comparator.comparing(Room::getCapacity));
                break;
            case "stars":
                availableRooms.sort(Comparator.comparing(Room::getStars).reversed());
                break;
            default:
                availableRooms.sort(Comparator.comparing(Room::getNumber));
        }

        System.out.println("\n=== Available Rooms (sorted by " + sortBy + ") ===");
        availableRooms.forEach(System.out::println);
    }

    public void displayRegularGuestsSorted(String sortBy) {
        // Find guests who stayed more than once
        Map<Guest, Long> guestStayCount = roomManager.getAllRooms().stream()
                .flatMap(room -> room.getStayHistory().stream())
                .collect(Collectors.groupingBy(StayRecord::getGuest, Collectors.counting()));

        List<Guest> regularGuests = guestStayCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        switch (sortBy.toLowerCase()) {
            case "name":
                regularGuests.sort(Comparator.comparing(Guest::getName));
                break;
            case "checkout":
                // Sort by last checkout date
                regularGuests.sort((g1, g2) -> {
                    LocalDate date1 = getLastCheckoutDate(g1);
                    LocalDate date2 = getLastCheckoutDate(g2);
                    return date2.compareTo(date1); // Newest first
                });
                break;
            default:
                regularGuests.sort(Comparator.comparing(Guest::getId));
        }

        System.out.println("\n=== Regular Guests (sorted by " + sortBy + ") ===");
        regularGuests.forEach(g -> System.out.println(g + " - stays: " + guestStayCount.get(g)));
    }

    private LocalDate getLastCheckoutDate(Guest guest) {
        return roomManager.getAllRooms().stream()
                .flatMap(room -> room.getStayHistory().stream())
                .filter(record -> record.getGuest().equals(guest))
                .map(StayRecord::getCheckOutDate)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.MIN);
    }

    public void displayTotalAvailableRooms() {
        long count = roomManager.getAllRooms().stream().filter(Room::isAvailable).count();
        System.out.println("\n=== Total Available Rooms ===");
        System.out.println("Available rooms: " + count);
    }

    public void displayTotalGuests() {
        int totalCurrentGuests = roomManager.getAllRooms().stream()
                .mapToInt(Room::getCurrentGuestCount)  // Use new method
                .sum();

        System.out.println("\n=== Total Current Guests ===");
        System.out.println("Current guests in hotel: " + totalCurrentGuests);

        // Show details by room
        System.out.println("Details by room:");
        roomManager.getAllRooms().stream()
                .filter(room -> room.getCurrentGuestCount() > 0)
                .forEach(room -> System.out.printf(" - Room %s: %d guests%n",
                        room.getNumber(), room.getCurrentGuestCount()));
    }

    public void displayRoomsAvailableByDate(LocalDate date) {
        List<Room> availableRooms = roomManager.getAllRooms().stream()
                .filter(room -> room.willBeAvailableByDate(date))
                .collect(Collectors.toList());

        System.out.println("\n=== Rooms Available by " + date + " ===");
        availableRooms.forEach(System.out::println);
    }

    public void displayGuestPayment(String roomNumber) {
        Room room = roomManager.getRoom(roomNumber);
        if (room != null && room.getStatus() == RoomStatus.OCCUPIED) {
            double cost = room.getCurrentGuestCost();
            System.out.println("\n=== Payment for Room " + roomNumber + " ===");
            System.out.printf("Guests: %s%n", room.getCurrentGuests().stream().map(Guest::getName).collect(Collectors.joining(", ")));
            System.out.printf("Total amount due: %.2f%n", cost);
        } else {
            System.out.println("Room " + roomNumber + " is not occupied");
        }
    }

    public void displayLastThreeGuests(String roomNumber) {
        Room room = roomManager.getRoom(roomNumber);
        if (room != null) {
            List<StayRecord> lastGuests = room.getLastGuests(3);
            System.out.println("\n=== Last 3 Guests of Room " + roomNumber + " ===");
            if (lastGuests.isEmpty()) {
                System.out.println("No stay history available");
            } else {
                lastGuests.forEach(System.out::println);
            }
        } else {
            System.out.println("Room " + roomNumber + " not found");
        }
    }

    public void displayGuestServices(String roomNumber, String sortBy) {
        Room room = roomManager.getRoom(roomNumber);
        if (room != null && room.getStatus() == RoomStatus.OCCUPIED) {
            List<Service> services = new ArrayList<>(room.getGuestServices());

            switch (sortBy.toLowerCase()) {
                case "price":
                    services.sort(Comparator.comparing(Service::getPrice));
                    break;
                case "name":
                    services.sort(Comparator.comparing(Service::getName));
                    break;
                default:
                    // no sorting
            }

            System.out.println("\n=== Services for Guests " + room.getCurrentGuests().stream().map(Guest::getName).collect(Collectors.joining(", ")) + " ===");
            if (services.isEmpty()) {
                System.out.println("No services ordered");
            } else {
                services.forEach(System.out::println);
                double total = services.stream().mapToDouble(Service::getPrice).sum();
                System.out.printf("Total services cost: %.2f%n", total);
            }
        } else {
            System.out.println("Room " + roomNumber + " is not occupied");
        }
    }

    public void displayPrices(String category, String sortBy) {
        if ("rooms".equalsIgnoreCase(category)) {
            List<Room> rooms = roomManager.getAllRooms();
            switch (sortBy.toLowerCase()) {
                case "price":
                    rooms.sort(Comparator.comparing(Room::getPrice));
                    break;
                case "type":
                    rooms.sort(Comparator.comparing(Room::getType));
                    break;
                default:
                    rooms.sort(Comparator.comparing(Room::getNumber));
            }
            System.out.println("\n=== Room Prices (sorted by " + sortBy + ") ===");
            rooms.forEach(r -> System.out.printf("%s - %.2f/night%n", r.getNumber(), r.getPrice()));
        }
    }

    public void displayRoomDetails(String roomNumber) {
        Room room = roomManager.getRoom(roomNumber);
        if (room != null) {
            System.out.println("\n=== Room " + roomNumber + " Details ===");
            System.out.println(room);
            System.out.println("Stay history: " + room.getStayHistory().size() + " records");
            if (room.getStatus() == RoomStatus.OCCUPIED) {
                System.out.println("Current guests: " + room.getCurrentGuests().stream().map(Guest::getName).collect(Collectors.joining(", ")));
                System.out.printf("Current cost: %.2f%n", room.getCurrentGuestCost());
            }
        } else {
            System.out.println("Room " + roomNumber + " not found");
        }
    }
}