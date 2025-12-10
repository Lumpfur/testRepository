package config;

import config.annotations.ConfigProperty;
import config.annotations.PropertyType;

public class HotelConfig {
    private static HotelConfig instance;

    @ConfigProperty(propertyName = "hotel.room.status.change.enabled")
    private boolean roomStatusChangeEnabled = true;

    @ConfigProperty(propertyName = "hotel.room.history.size")
    private int roomHistorySize = 10;

    @ConfigProperty(propertyName = "hotel.save.on.exit")
    private boolean saveOnExit = true;

    @ConfigProperty(propertyName = "hotel.auto.backup")
    private boolean autoBackup = false;

    @ConfigProperty(propertyName = "hotel.backup.interval")
    private int backupInterval = 3600;

    @ConfigProperty(propertyName = "hotel.default.checkout.time", type = PropertyType.STRING)
    private String defaultCheckoutTime = "12:00";

    @ConfigProperty(propertyName = "hotel.max.guests.per.room")
    private int maxGuestsPerRoom = 4;

    @ConfigProperty(propertyName = "hotel.enable.services", type = PropertyType.BOOLEAN)
    private boolean enableServices = true;

    private HotelConfig() {
        ConfigLoader.loadConfig(this);
        System.out.println("Configuration loaded successfully");
        printConfiguration();
    }

    public static HotelConfig getInstance() {
        if (instance == null) {
            instance = new HotelConfig();
        }
        return instance;
    }

    public boolean isRoomStatusChangeEnabled() {
        return roomStatusChangeEnabled;
    }

    public int getRoomHistorySize() {
        return roomHistorySize;
    }

    public boolean isSaveOnExit() {
        return saveOnExit;
    }

    public boolean isAutoBackup() {
        return autoBackup;
    }

    public int getBackupInterval() {
        return backupInterval;
    }

    public String getDefaultCheckoutTime() {
        return defaultCheckoutTime;
    }

    public int getMaxGuestsPerRoom() {
        return maxGuestsPerRoom;
    }

    public boolean isEnableServices() {
        return enableServices;
    }

    public void reloadConfiguration() {
        ConfigLoader.loadConfig(this);
        System.out.println("Configuration reloaded from file");
        printConfiguration();
    }

    public void updateConfiguration(boolean statusChangeEnabled, int historySize) {
        this.roomStatusChangeEnabled = statusChangeEnabled;
        this.roomHistorySize = historySize;
        System.out.println("Configuration updated in memory");
    }

    public void updateConfiguration(String propertyName, Object value) {
        try {
            java.lang.reflect.Field field = this.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(this, value);
            System.out.println("Configuration updated: " + propertyName + " = " + value);
        } catch (Exception e) {
            System.err.println("Error updating configuration: " + e.getMessage());
        }
    }

    private void printConfiguration() {
        System.out.println("\n=== Current Configuration ===");
        System.out.println("Room Status Change Enabled: " + roomStatusChangeEnabled);
        System.out.println("Room History Size: " + roomHistorySize);
        System.out.println("Save on Exit: " + saveOnExit);
        System.out.println("Auto Backup: " + autoBackup);
        System.out.println("Backup Interval: " + backupInterval + " seconds");
        System.out.println("Default Checkout Time: " + defaultCheckoutTime);
        System.out.println("Max Guests per Room: " + maxGuestsPerRoom);
        System.out.println("Enable Services: " + enableServices);
        System.out.println("=============================\n");
    }
}