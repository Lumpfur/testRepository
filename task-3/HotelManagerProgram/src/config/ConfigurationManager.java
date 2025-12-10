package config;

import config.annotations.ConfigProperty;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class ConfigurationManager {
    private static ConfigurationManager instance;
    private Map<String, Object> configObjects = new HashMap<>();
    private Properties globalProperties = new Properties();

    private ConfigurationManager() {
        loadAllConfigurations();
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    private void loadAllConfigurations() {
        registerConfig("hotel", HotelConfig.getInstance());
        registerConfig("room", new RoomConfig());
        registerConfig("service", new ServiceConfig());

        loadGlobalProperties();
    }

    private void registerConfig(String name, Object configObject) {
        configObjects.put(name, configObject);
    }

    public <T> T getConfig(Class<T> configClass) {
        for (Object config : configObjects.values()) {
            if (configClass.isInstance(config)) {
                return configClass.cast(config);
            }
        }
        return null;
    }

    public Object getConfig(String name) {
        return configObjects.get(name);
    }

    public void reloadAllConfigurations() {
        for (Object config : configObjects.values()) {
            ConfigLoader.loadConfig(config);
        }
        loadGlobalProperties();
        System.out.println("All configurations reloaded");
    }

    private void loadGlobalProperties() {
        try {
            File globalConfig = new File("global.properties");
            if (globalConfig.exists()) {
                globalProperties.load(new java.io.FileInputStream(globalConfig));
            }
        } catch (Exception e) {
            System.err.println("Error loading global properties: " + e.getMessage());
        }
    }

    public String getGlobalProperty(String key) {
        return globalProperties.getProperty(key);
    }

    public void setGlobalProperty(String key, String value) {
        globalProperties.setProperty(key, value);
    }

    public void saveAllConfigurations() {
        for (Map.Entry<String, Object> entry : configObjects.entrySet()) {
            String fileName = entry.getKey() + ".properties";
            ConfigFactory.saveConfigToFile(entry.getValue(), fileName);
        }
        saveGlobalProperties();
        System.out.println("All configurations saved to files");
    }

    private void saveGlobalProperties() {
        try {
            globalProperties.store(new java.io.FileOutputStream("global.properties"),
                    "Global Configuration");
        } catch (Exception e) {
            System.err.println("Error saving global properties: " + e.getMessage());
        }
    }

    public void printAllConfigurations() {
        System.out.println("\n=== ALL CONFIGURATIONS ===");
        for (Map.Entry<String, Object> entry : configObjects.entrySet()) {
            System.out.println("\n[" + entry.getKey().toUpperCase() + " CONFIG]");
            printConfigObject(entry.getValue());
        }
        System.out.println("==========================\n");
    }

    private void printConfigObject(Object configObject) {
        try {
            Class<?> clazz = configObject.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ConfigProperty.class)) {
                    field.setAccessible(true);
                    System.out.printf("  %-30s = %s%n", field.getName(), field.get(configObject));
                }
            }
        } catch (Exception e) {
            System.err.println("Error printing configuration: " + e.getMessage());
        }
    }
}