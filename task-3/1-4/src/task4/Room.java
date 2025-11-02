package task4;
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
    private Guest currentGuest;
    private List<StayRecord> stayHistory;
    private List<Service> guestServices;

    public Room(String number, String type, double price, int capacity, int stars) {
        this.number = number;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        this.status = RoomStatus.AVAILABLE;
        this.currentGuest = null;
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
    public Guest getCurrentGuest() { return currentGuest; }
    public List<StayRecord> getStayHistory() { return new ArrayList<>(stayHistory); }
    public List<Service> getGuestServices() { return new ArrayList<>(guestServices); }

    // Setters
    public void setNumber(String number) { this.number = number; }
    public void setType(String type) { this.type = type; }
    public void setPrice(double price) { this.price = price; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setStars(int stars) { this.stars = stars; }

    public void setStatus(RoomStatus status) {
        this.status = status;
        if (status != RoomStatus.OCCUPIED) {
            this.currentGuest = null;
        }
    }

    // Guest management methods
    public boolean checkIn(Guest guest) {
        if (status == RoomStatus.AVAILABLE && guest != null) {
            this.currentGuest = guest;
            this.status = RoomStatus.OCCUPIED;
            StayRecord record = new StayRecord(guest, LocalDate.now());
            stayHistory.add(record);
            this.guestServices.clear();
            return true;
        }
        return false;
    }

    public boolean checkOut() {
        if (status == RoomStatus.OCCUPIED && !stayHistory.isEmpty()) {
            StayRecord lastRecord = stayHistory.get(stayHistory.size() - 1);
            lastRecord.setCheckOutDate(LocalDate.now());

            // Calculate stay cost
            long stayDays = lastRecord.getStayDuration();
            double roomCost = stayDays * price;
            double servicesCost = guestServices.stream().mapToDouble(Service::getPrice).sum();
            lastRecord.setTotalCost(roomCost + servicesCost);

            this.currentGuest = null;
            this.status = RoomStatus.AVAILABLE;
            this.guestServices.clear();
            return true;
        }
        return false;
    }

    // Add service for current guest
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
            StayRecord lastRecord = stayHistory.get(stayHistory.size() - 1);
            return lastRecord.getCheckOutDate() != null &&
                    lastRecord.getCheckOutDate().isBefore(date);
        }
        return false;
    }

    // Get current guest cost
    public double getCurrentGuestCost() {
        if (status == RoomStatus.OCCUPIED && !stayHistory.isEmpty()) {
            StayRecord lastRecord = stayHistory.get(stayHistory.size() - 1);
            long stayDays = ChronoUnit.DAYS.between(lastRecord.getCheckInDate(), LocalDate.now());
            double roomCost = stayDays * price;
            double servicesCost = guestServices.stream().mapToDouble(Service::getPrice).sum();
            return roomCost + servicesCost;
        }
        return 0.0;
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
        String guestInfo = (currentGuest != null) ? ", guest: " + currentGuest.getName() : "";
        return String.format("Room[%s, %s, %d stars, %d persons, %.2f, %s%s]",
                number, type, stars, capacity, price, status.getDescription(), guestInfo);
    }
}