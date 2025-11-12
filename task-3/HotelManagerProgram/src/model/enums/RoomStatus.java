package model.enums;

public enum RoomStatus {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    UNDER_MAINTENANCE("Under Maintenance"),
    UNDER_SERVICE("Under Service");

    private final String description;

    RoomStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}