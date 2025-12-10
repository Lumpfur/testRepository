package model.entity;

import config.RoomConfig;
import model.enums.RoomStatus;
import config.HotelConfig;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final HotelConfig hotelConfig = HotelConfig.getInstance();
    private static final RoomConfig roomConfig = new RoomConfig();

    private String number;
    private String type;
    private double price;
    private int capacity;
    private int stars;
    private RoomStatus status;
    private List<Guest> currentGuests;
    private List<StayRecord> stayHistory;
    private List<Service> guestServices;

    public Room(String number, String type) {
        this(number, type,
                roomConfig.getMinPrice(),
                roomConfig.getDefaultCapacity(),
                roomConfig.getDefaultStars());
    }

    public Room(String number, String type, double price) {
        this(number, type, price,
                roomConfig.getDefaultCapacity(),
                roomConfig.getDefaultStars());
    }

    public Room(String number, String type, double price, int capacity, int stars) {
        this.number = number;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        this.status = RoomStatus.AVAILABLE;
        this.currentGuests = new ArrayList<>();
        this.stayHistory = new ArrayList<>();
        this.guestServices = new ArrayList<>();

        //checking config limits
        validateConfiguration();
    }

    private void validateConfiguration() {
        if (capacity > hotelConfig.getMaxGuestsPerRoom()) {
            System.out.println("Warning: Room capacity exceeds maximum allowed (" +
                    hotelConfig.getMaxGuestsPerRoom() + ")");
        }

        if (price < roomConfig.getMinPrice() || price > roomConfig.getMaxPrice()) {
            System.out.println("Warning: Room price is outside allowed range (" +
                    roomConfig.getMinPrice() + " - " + roomConfig.getMaxPrice() + ")");
        }
    }

    public void updateConfiguration() {
        enforceHistoryLimit();
    }

    private void enforceHistoryLimit() {
        int maxSize = hotelConfig.getRoomHistorySize();
        if (stayHistory.size() > maxSize) {
            stayHistory = new ArrayList<>(stayHistory.subList(stayHistory.size() - maxSize, stayHistory.size()));
        }
    }

    public static RoomConfig getRoomConfig() {
        return roomConfig;
    }

    public String getNumber() { return number; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public int getCapacity() { return capacity; }
    public int getStars() { return stars; }
    public RoomStatus getStatus() { return status; }
    public List<Guest> getCurrentGuests() { return new ArrayList<>(currentGuests); }
    public List<StayRecord> getStayHistory() { return new ArrayList<>(stayHistory); }
    public List<Service> getGuestServices() { return new ArrayList<>(guestServices); }

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
            this.currentGuests.clear();
        }
    }

    public boolean checkIn(Guest guest) {
        if (status == RoomStatus.AVAILABLE && guest != null && currentGuests.size() < capacity) {
            this.currentGuests.add(guest);
            this.status = RoomStatus.OCCUPIED;
            StayRecord record = new StayRecord(guest, LocalDate.now());
            stayHistory.add(record);

            enforceHistoryLimit();
            this.guestServices.clear();
            return true;
        }
        return false;
    }

    public boolean checkIn(List<Guest> guests) {
        if (status == RoomStatus.AVAILABLE && guests != null && !guests.isEmpty()
                && guests.size() <= capacity) {
            this.currentGuests.addAll(guests);
            this.status = RoomStatus.OCCUPIED;

            for (Guest guest : guests) {
                StayRecord record = new StayRecord(guest, LocalDate.now());
                stayHistory.add(record);
            }

            enforceHistoryLimit();
            this.guestServices.clear();
            return true;
        }
        return false;
    }

    public boolean checkOut() {
        if (status == RoomStatus.OCCUPIED && !stayHistory.isEmpty()) {
            LocalDate checkoutDate = LocalDate.now();
            for (int i = stayHistory.size() - currentGuests.size(); i < stayHistory.size(); i++) {
                StayRecord record = stayHistory.get(i);
                if (record.getCheckOutDate() == null) {
                    record.setCheckOutDate(checkoutDate);

                    long stayDays = record.getStayDuration();
                    double roomCost = stayDays * price / currentGuests.size();
                    double servicesCost = guestServices.stream()
                            .mapToDouble(Service::getPrice)
                            .sum() / currentGuests.size();
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

    public boolean removeGuest(Guest guest) {
        boolean removed = currentGuests.remove(guest);
        if (currentGuests.isEmpty()) {
            this.status = RoomStatus.AVAILABLE;
        }
        return removed;
    }

    public boolean addService(Service service) {
        if (status == RoomStatus.OCCUPIED && service != null) {
            guestServices.add(service);
            return true;
        }
        return false;
    }

    public List<StayRecord> getLastGuests(int count) {
        return stayHistory.stream()
                .skip(Math.max(0, stayHistory.size() - count))
                .limit(count)
                .collect(Collectors.toList());
    }

    public boolean willBeAvailableByDate(LocalDate date) {
        if (status == RoomStatus.AVAILABLE) {
            return true;
        }
        if (status == RoomStatus.OCCUPIED && !stayHistory.isEmpty()) {
            return stayHistory.stream()
                    .skip(stayHistory.size() - currentGuests.size())
                    .allMatch(record -> record.getCheckOutDate() != null &&
                            record.getCheckOutDate().isBefore(date));
        }
        return false;
    }

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

    public int getCurrentGuestCount() {
        return currentGuests.size();
    }

    public boolean hasAvailableSpace() {
        return currentGuests.size() < capacity;
    }

    public int getAvailableSpaces() {
        return capacity - currentGuests.size();
    }

    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }

    private void enforceHistoryLimit() {
        int maxSize = HotelConfig.getInstance().getRoomHistorySize();
        if (stayHistory.size() > maxSize) {
            stayHistory = new ArrayList<>(stayHistory.subList(stayHistory.size() - maxSize, stayHistory.size()));
        }
    }

    public void updateConfiguration() {
        enforceHistoryLimit();
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