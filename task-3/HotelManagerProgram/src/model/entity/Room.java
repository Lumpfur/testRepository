package model.entity;

import model.enums.RoomStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Room {
    private String number;
    private String type;
    private double price;
    private int capacity;
    private int stars;
    private RoomStatus status;
    private List<Guest> currentGuests;  // Changed from Guest currentGuest to List<Guest>
    private List<StayRecord> stayHistory;
    private List<Service> guestServices;

    public Room(String number, String type, double price, int capacity, int stars) {
        this.number = number;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        this.status = RoomStatus.AVAILABLE;
        this.currentGuests = new ArrayList<>();  // Initialize as empty list
        this.stayHistory = new ArrayList<>();
        this.guestServices = new ArrayList<>();
    }

    public Room(String number, String type, double price) {
        this(number, type, price, 2, 3);
    }

    // Getters
    public String getNumber() { return number; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public int getCapacity() { return capacity; }
    public int getStars() { return stars; }
    public RoomStatus getStatus() { return status; }
    public List<Guest> getCurrentGuests() { return new ArrayList<>(currentGuests); }  // Return copy of list
    public List<StayRecord> getStayHistory() { return new ArrayList<>(stayHistory); }
    public List<Service> getGuestServices() { return new ArrayList<>(guestServices); }

    // Get first guest (for backward compatibility)
    public Guest getCurrentGuest() {
        return currentGuests.isEmpty() ? null : currentGuests.get(0);
    }

    public void setNumber(String number) { this.number = number; }
    public void setType(String type) { this.type = type; }
    public void setPrice(double price) { this.price = price; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setStars(int stars) { this.stars = stars; }

    public void setStatus(RoomStatus status) {
        this.status = status;
        if (status != RoomStatus.OCCUPIED) {
            this.currentGuests.clear();  // Clear all guests when status changes
        }
    }

    // Guest management methods - updated for multiple guests
    public boolean checkIn(Guest guest) {
        if (status == RoomStatus.AVAILABLE && guest != null && currentGuests.size() < capacity) {
            this.currentGuests.add(guest);
            this.status = RoomStatus.OCCUPIED;
            StayRecord record = new StayRecord(guest, LocalDate.now());
            stayHistory.add(record);
            this.guestServices.clear();
            return true;
        }
        return false;
    }

    // Check in multiple guests at once
    public boolean checkIn(List<Guest> guests) {
        if (status == RoomStatus.AVAILABLE && guests != null && !guests.isEmpty()
                && guests.size() <= capacity) {
            this.currentGuests.addAll(guests);
            this.status = RoomStatus.OCCUPIED;
            // Create stay records for all guests
            for (Guest guest : guests) {
                StayRecord record = new StayRecord(guest, LocalDate.now());
                stayHistory.add(record);
            }
            this.guestServices.clear();
            return true;
        }
        return false;
    }

    public boolean checkOut() {
        if (status == RoomStatus.OCCUPIED && !stayHistory.isEmpty()) {
            // Update all recent stay records with checkout date
            LocalDate checkoutDate = LocalDate.now();
            for (int i = stayHistory.size() - currentGuests.size(); i < stayHistory.size(); i++) {
                StayRecord record = stayHistory.get(i);
                if (record.getCheckOutDate() == null) {
                    record.setCheckOutDate(checkoutDate);

                    // Calculate stay cost for each guest
                    long stayDays = record.getStayDuration();
                    double roomCost = stayDays * price / currentGuests.size(); // Split room cost
                    double servicesCost = guestServices.stream()
                            .mapToDouble(Service::getPrice)
                            .sum() / currentGuests.size(); // Split services cost
                    record.setTotalCost(roomCost + servicesCost);
                }
            }

            this.currentGuests.clear();
            this.status = RoomStatus.AVAILABLE;
            this.guestServices.clear();
            return true;
        }
        return false;
    }

    // Remove specific guest from room
    public boolean removeGuest(Guest guest) {
        boolean removed = currentGuests.remove(guest);
        if (currentGuests.isEmpty()) {
            this.status = RoomStatus.AVAILABLE;
        }
        return removed;
    }

    // Add service for current guests
    public boolean addService(Service service) {
        if (status == RoomStatus.OCCUPIED && service != null) {
            guestServices.add(service);
            return true;
        }
        return false;
    }

    // Get last N guests
    public List<StayRecord> getLastGuests(int count) {
        return stayHistory.stream()
                .skip(Math.max(0, stayHistory.size() - count))
                .limit(count)
                .collect(Collectors.toList());
    }

    // Check if room will be available by specific date
    public boolean willBeAvailableByDate(LocalDate date) {
        if (status == RoomStatus.AVAILABLE) {
            return true;
        }
        if (status == RoomStatus.OCCUPIED && !stayHistory.isEmpty()) {
            // Check if all current guests will be checked out by the date
            return stayHistory.stream()
                    .skip(stayHistory.size() - currentGuests.size())
                    .allMatch(record -> record.getCheckOutDate() != null &&
                            record.getCheckOutDate().isBefore(date));
        }
        return false;
    }

    // Get current guest cost (total for all guests in room)
    public double getCurrentGuestCost() {
        if (status == RoomStatus.OCCUPIED && !stayHistory.isEmpty()) {
            double totalCost = 0.0;
            for (int i = stayHistory.size() - currentGuests.size(); i < stayHistory.size(); i++) {
                StayRecord record = stayHistory.get(i);
                long stayDays = ChronoUnit.DAYS.between(record.getCheckInDate(), LocalDate.now());
                double roomCost = stayDays * price / currentGuests.size();
                double servicesCost = guestServices.stream()
                        .mapToDouble(Service::getPrice)
                        .sum() / currentGuests.size();
                totalCost += roomCost + servicesCost;
            }
            return totalCost;
        }
        return 0.0;
    }

    // Get number of current guests
    public int getCurrentGuestCount() {
        return currentGuests.size();
    }

    // Check if room has available space
    public boolean hasAvailableSpace() {
        return currentGuests.size() < capacity;
    }

    // Get available spaces count
    public int getAvailableSpaces() {
        return capacity - currentGuests.size();
    }

    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(number, room.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        String guestInfo = !currentGuests.isEmpty() ?
                ", guests: " + currentGuests.stream().map(Guest::getName).collect(Collectors.joining(", ")) +
                        " (" + currentGuests.size() + "/" + capacity + ")" : "";
        return String.format("Room[%s, %s, %d stars, %d persons, %.2f, %s%s]",
                number, type, stars, capacity, price, status.getDescription(), guestInfo);
    }
}