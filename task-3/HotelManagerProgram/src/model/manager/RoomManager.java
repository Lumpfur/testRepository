package model.manager;

import model.enums.RoomStatus;
import model.entity.*;

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

    // New method: check in multiple guests to a room
    public boolean checkInGuests(String roomNumber, List<Guest> guests) {
        Room room = rooms.get(roomNumber);
        return room != null && room.checkIn(guests);
    }

    // New method: add guest to existing room (if there's space)
    public boolean addGuestToRoom(String roomNumber, Guest guest) {
        Room room = rooms.get(roomNumber);
        if (room != null && room.hasAvailableSpace() && room.getStatus() == RoomStatus.OCCUPIED) {
            return room.checkIn(guest);
        }
        return false;
    }

    // New method: remove specific guest from room
    public boolean removeGuestFromRoom(String roomNumber, Guest guest) {
        Room room = rooms.get(roomNumber);
        return room != null && room.removeGuest(guest);
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

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    // New method: get total number of current guests in hotel
    public int getTotalCurrentGuests() {
        return rooms.values().stream()
                .mapToInt(Room::getCurrentGuestCount)
                .sum();
    }
}