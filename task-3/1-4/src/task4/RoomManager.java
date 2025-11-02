package task4;
import java.util.*;

public class RoomManager {
    private Map<String, Room> rooms;

    public RoomManager() {
        this.rooms = new HashMap<>();
    }

    // Add room
    public boolean addRoom(Room room) {
        if (room != null && !rooms.containsKey(room.getNumber())) {
            rooms.put(room.getNumber(), room);
            return true;
        }
        return false;
    }

    // Remove room
    public boolean removeRoom(String roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room != null && room.getStatus() != RoomStatus.OCCUPIED) {
            rooms.remove(roomNumber);
            return true;
        }
        return false;
    }

    public boolean checkInGuest(String roomNumber, Guest guest) {
        Room room = rooms.get(roomNumber);
        return room != null && room.checkIn(guest);
    }

    public boolean checkOutGuest(String roomNumber) {
        Room room = rooms.get(roomNumber);
        return room != null && room.checkOut();
    }

    public boolean changeRoomStatus(String roomNumber, RoomStatus newStatus) {
        Room room = rooms.get(roomNumber);
        if (room != null) {
            // Cannot change to maintenance/service if room is occupied
            if (room.getStatus() == RoomStatus.OCCUPIED &&
                    (newStatus == RoomStatus.UNDER_MAINTENANCE ||
                            newStatus == RoomStatus.UNDER_SERVICE)) {
                return false;
            }
            room.setStatus(newStatus);
            return true;
        }
        return false;
    }

    public boolean changeRoomPrice(String roomNumber, double newPrice) {
        Room room = rooms.get(roomNumber);
        if (room != null) {
            room.setPrice(newPrice);
            return true;
        }
        return false;
    }

    // Get room
    public Room getRoom(String roomNumber) {
        return rooms.get(roomNumber);
    }

    // Get all rooms
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }
}