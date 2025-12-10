package config;

import config.annotations.ConfigProperty;
import config.annotations.PropertyType;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class ConfigLoader {

    public static void loadConfig(Object configObject) {
        loadConfig(configObject, "hotel.properties");
    }

    public static void loadConfig(Object configObject, String defaultConfigFile) {
        Class<?> clazz = configObject.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
                processField(configObject, field, annotation, defaultConfigFile);
            }
        }
    }

    private static void processField(Object configObject, Field field,
                                     ConfigProperty annotation, String defaultConfigFile) {
        try {
            String configFileName = annotation.configFileName();
            if (configFileName.isEmpty()) {
                configFileName = defaultConfigFile;
            }

            String propertyKey = annotation.propertyName();
            if (propertyKey.isEmpty()) {
                propertyKey = configObject.getClass().getSimpleName() + "." + field.getName();
            }

            Properties properties = loadProperties(configFileName);
            String value = properties.getProperty(propertyKey);

            if (value != null) {
                setFieldValue(configObject, field, value, annotation.type());
            } else {
                // Если значение не найдено, используем значение по умолчанию из поля
                System.out.println("Config property not found: " + propertyKey +
                        ", using default value");
            }

        } catch (Exception e) {
            System.err.println("Error loading config for field " + field.getName() + ": " + e.getMessage());
        }
    }

    private static Properties loadProperties(String configFileName) throws IOException {
        Properties properties = new Properties();
        File configFile = new File(configFileName);

        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                properties.load(input);
            }
        } else {
            System.out.println("Config file not found: " + configFileName +
                    ", using default values");
        }

        return properties;
    }

    private static void setFieldValue(Object configObject, Field field,
                                      String value, PropertyType type) throws Exception {
        field.setAccessible(true);
        Class<?> fieldType = field.getType();

        Object convertedValue;
        if (type == PropertyType.AUTO) {
            convertedValue = convertAuto(value, fieldType);
        } else {
            convertedValue = convertByType(value, type, fieldType);
        }

        if (convertedValue != null) {
            field.set(configObject, convertedValue);
        }
    }

    private static Object convertAuto(String value, Class<?> targetType) {
        try {
            if (targetType == String.class) {
                return value;
            } else if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(value.trim());
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(value.trim());
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(value.trim());
            } else if (targetType == float.class || targetType == Float.class) {
                return Float.parseFloat(value.trim());
            } else if (targetType == long.class || targetType == Long.class) {
                return Long.parseLong(value.trim());
            } else if (targetType == List.class) {
                return Arrays.asList(value.split("\\s*,\\s*"));
            } else if (targetType.isArray() && targetType.getComponentType() == String.class) {
                return value.split("\\s*,\\s*");
            } else if (targetType.isEnum()) {
                return Enum.valueOf((Class<Enum>) targetType, value.trim().toUpperCase());
            } else {
                System.err.println("Unsupported type for auto conversion: " + targetType.getName());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error converting value '" + value + "' to type " + targetType.getName());
            return null;
        }
    }

    private static Object convertByType(String value, PropertyType type, Class<?> targetType) {
        try {
            switch (type) {
                case STRING:
                    return value;
                case INTEGER:
                    return Integer.parseInt(value.trim());
                case BOOLEAN:
                    return Boolean.parseBoolean(value.trim());
                case DOUBLE:
                    return Double.parseDouble(value.trim());
                case LIST:
                    if (targetType == List.class) {
                        return Arrays.asList(value.split("\\s*,\\s*"));
                    }
                    break;
                case ARRAY:
                    if (targetType.isArray() && targetType.getComponentType() == String.class) {
                        return value.split("\\s*,\\s*");
                    }
                    break;
            }
            return value;
        } catch (Exception e) {
            System.err.println("Error converting value by type: " + e.getMessage());
            return null;
        }
    }

    public static void loadConfigFromMap(Object configObject, Map<String, String> configMap) {
        Class<?> clazz = configObject.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);

                String propertyKey = annotation.propertyName();
                if (propertyKey.isEmpty()) {
                    propertyKey = configObject.getClass().getSimpleName() + "." + field.getName();
                }

                String value = configMap.get(propertyKey);
                if (value != null) {
                    try {
                        field.setAccessible(true);
                        Object convertedValue = convertAuto(value, field.getType());
                        if (convertedValue != null) {
                            field.set(configObject, convertedValue);
                        }
                    } catch (Exception e) {
                        System.err.println("Error setting field " + field.getName() + ": " + e.getMessage());
                    }
                }
            }
        }
    }
}