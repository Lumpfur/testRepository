package task4;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StayRecord {
    private Guest guest;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalCost;

    public StayRecord(Guest guest, LocalDate checkInDate) {
        this.guest = guest;
        this.checkInDate = checkInDate;
        this.totalCost = 0.0;
    }

    public StayRecord(Guest guest, LocalDate checkInDate, LocalDate checkOutDate) {
        this.guest = guest;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalCost = 0.0;
    }

    public Guest getGuest() { return guest; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public double getTotalCost() { return totalCost; }

    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public long getStayDuration() {
        if (checkOutDate != null && checkInDate != null) {
            return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("StayRecord[Guest: %s, Check-in: %s, Check-out: %s, Total: %.2f]",
                guest.getName(), checkInDate, checkOutDate, totalCost);
    }
}