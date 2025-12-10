package config;

import config.annotations.ConfigProperty;

import java.lang.reflect.Field;

public class ConfigFactory {

    public static <T> T createConfig(Class<T> configClass) {
        try {
            T config = configClass.getDeclaredConstructor().newInstance();
            ConfigLoader.loadConfig(config);
            return config;
        } catch (Exception e) {
            System.err.println("Error creating config object: " + e.getMessage());
            return null;
        }
    }

    public static void reloadConfig(Object configObject) {
        ConfigLoader.loadConfig(configObject);
    }

    public static void saveConfigToFile(Object configObject, String fileName) {
        try {
            Class<?> clazz = configObject.getClass();
            StringBuilder configContent = new StringBuilder();
            configContent.append("# Configuration for ").append(clazz.getSimpleName()).append("\n");
            configContent.append("# Generated on: ").append(new java.util.Date()).append("\n\n");

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ConfigProperty.class)) {
                    ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
                    field.setAccessible(true);

                    String propertyKey = annotation.propertyName();
                    if (propertyKey.isEmpty()) {
                        propertyKey = clazz.getSimpleName() + "." + field.getName();
                    }

                    Object value = field.get(configObject);
                    configContent.append(propertyKey).append("=").append(value).append("\n");
                }
            }

            try (java.io.FileWriter writer = new java.io.FileWriter(fileName)) {
                writer.write(configContent.toString());
                System.out.println("Configuration saved to: " + fileName);
            }

        } catch (Exception e) {
            System.err.println("Error saving configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}