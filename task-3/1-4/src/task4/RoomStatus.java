package task4;

public enum RoomStatus {
    AVAILABLE("Free"),
    OCCUPIED("Booked"),
    UNDER_MAINTENANCE("Tech-support"),
    UNDER_SERVICE("Service-support");

    private final String description;

    RoomStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
