package task4;
import java.util.Objects;

public class Room {
    private String number;
    private String type;
    private double price;
    private RoomStatus status;
    private Guest guest;

    public Room(String number, String type, double price) {
        this.number = number;
        this.type = type;
        this.price = price;
        this.status = RoomStatus.AVAILABLE;
        this.guest = null;
    }

    public String getNumber() {
        return number;
    }
    public String getType() {
        return type;
    }
    public double getPrice() {
        return price;
    }
    public RoomStatus getStatus() {
        return status;
    }
    public Guest getGuest() {
        return guest;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    //method for changing status
    public void setStatus(RoomStatus status) {
        this.status = status;
        if (status != RoomStatus.OCCUPIED) {
            this.guest = null;
        }
    }

    //method for the work with the guest
    public boolean checkIn(Guest guest) {
        if (status == RoomStatus.AVAILABLE && guest != null) {
            this.guest = guest;
            this.status = RoomStatus.OCCUPIED;
            return true;
        }
        return false;
    }

    public boolean checkOut() {
        if (status == RoomStatus.OCCUPIED) {
            this.guest = null;
            this.status = RoomStatus.AVAILABLE;
            return true;
        }
        return false;
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
        String guestInfo = (guest != null) ? ", гость: " + guest.getName() : "";
        return String.format("Номер[%s, %s, %.2f руб., %s%s]",
                number, type, price, status.getDescription(), guestInfo);
    }
}
