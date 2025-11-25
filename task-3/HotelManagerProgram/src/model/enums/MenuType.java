package model.enums;

public enum MenuType {
    MAIN("Main Menu"),
    GUEST("Guest Management"),
    ROOM("Room Management"),
    SERVICE("Service Management"),
    REPORT("Reports"),
    SUBMENU("Submenu");

    private final String displayName;

    MenuType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}