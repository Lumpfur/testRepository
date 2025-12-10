package config;

import config.annotations.ConfigProperty;
import config.annotations.PropertyType;

public class RoomConfig {

    @ConfigProperty(propertyName = "room.default.capacity")
    private int defaultCapacity = 2;

    @ConfigProperty(propertyName = "room.default.stars")
    private int defaultStars = 3;

    @ConfigProperty(propertyName = "room.types.allowed", type = PropertyType.LIST)
    private java.util.List<String> allowedRoomTypes;

    @ConfigProperty(propertyName = "room.min.price")
    private double minPrice = 50.0;

    @ConfigProperty(propertyName = "room.max.price")
    private double maxPrice = 500.0;

    public RoomConfig() {
        if (allowedRoomTypes == null) {
            allowedRoomTypes = java.util.Arrays.asList("Standard", "Deluxe", "Suite", "Family");
        }
        ConfigLoader.loadConfig(this, "room.properties");
    }

    public int getDefaultCapacity() {
        return defaultCapacity;
    }

    public int getDefaultStars() {
        return defaultStars;
    }

    public java.util.List<String> getAllowedRoomTypes() {
        return new java.util.ArrayList<>(allowedRoomTypes);
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }
}