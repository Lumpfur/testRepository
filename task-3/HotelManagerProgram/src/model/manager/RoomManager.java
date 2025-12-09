package model.manager;

import model.enums.RoomStatus;
import model.entity.*;
import config.HotelConfig;

import java.io.Serializable;
import java.util.*;

public class RoomManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Room> rooms;

    public RoomManager() {
        this.rooms = new HashMap<>();
    }

    public boolean addRoom(Room room) {
        if (room != null && !rooms.containsKey(room.getNumber())) {
            rooms.put(room.getNumber(), room);
            return true;
        }
        return false;
    }

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

    public boolean checkInGuests(String roomNumber, List<Guest> guests) {
        Room room = rooms.get(roomNumber);
        return room != null && room.checkIn(guests);
    }

    public boolean addGuestToRoom(String roomNumber, Guest guest) {
        Room room = rooms.get(roomNumber);
        if (room != null && room.hasAvailableSpace() && room.getStatus() == RoomStatus.OCCUPIED) {
            return room.checkIn(guest);
        }
        return false;
    }

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
            if (!HotelConfig.getInstance().isRoomStatusChangeEnabled()) {
                System.out.println("Room status change is disabled in configuration");
                return false;
            }

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

    public Room getRoom(String roomNumber) {
        return rooms.get(roomNumber);
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public int getTotalCurrentGuests() {
        return rooms.values().stream()
                .mapToInt(Room::getCurrentGuestCount)
                .sum();
    }
}