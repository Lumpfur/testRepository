package task4;

import java.util.*;

public class RoomManager {
    private Map<String, Room> rooms;

    public RoomManager() {
        this.rooms = new HashMap<>();
    }

    //adding the rooms
    public boolean addRoom(Room room) {
        if (room != null && !rooms.containsKey(room.getNumber())) {
            rooms.put(room.getNumber(), room);
            return true;
        }
        return false;
    }

    //remove the rooms
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
            // u cant switch the status to the under service/tech support with the booked room
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

    // get the room
    public Room getRoom(String roomNumber) {
        return rooms.get(roomNumber);
    }


    //get all the rooms
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }
}
