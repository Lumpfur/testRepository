package config;

import config.annotations.ValidateConfig;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ConfigValidator {

    public static boolean validate(Object configObject) {
        Class<?> clazz = configObject.getClass();
        boolean isValid = true;

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ValidateConfig.class)) {
                ValidateConfig annotation = field.getAnnotation(ValidateConfig.class);
                if (!validateField(configObject, field, annotation)) {
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    private static boolean validateField(Object configObject, Field field, ValidateConfig annotation) {
        try {
            field.setAccessible(true);
            Object value = field.get(configObject);

            if (annotation.required() && value == null) {
                System.err.println("Validation failed: Field " + field.getName() + " is required");
                return false;
            }

            if (value == null) {
                return true;
            }

            if (value instanceof Number) {
                double numValue = ((Number) value).doubleValue();
                if (numValue < annotation.min() || numValue > annotation.max()) {
                    System.err.println("Validation failed: Field " + field.getName() +
                            " value " + numValue + " not in range [" +
                            annotation.min() + ", " + annotation.max() + "]");
                    return false;
                }
            }

            if (value instanceof String) {
                String strValue = (String) value;
                if (strValue.length() < annotation.minLength() ||
                        strValue.length() > annotation.maxLength()) {
                    System.err.println("Validation failed: Field " + field.getName() +
                            " length not in range [" + annotation.minLength() +
                            ", " + annotation.maxLength() + "]");
                    return false;
                }

                if (annotation.allowedValues().length > 0) {
                    if (!Arrays.asList(annotation.allowedValues()).contains(strValue)) {
                        System.err.println("Validation failed: Field " + field.getName() +
                                " value '" + strValue + "' not in allowed values: " +
                                Arrays.toString(annotation.allowedValues()));
                        return false;
                    }
                }
            }

            return true;

        } catch (Exception e) {
            System.err.println("Validation error for field " + field.getName() + ": " + e.getMessage());
            return false;
        }
    }
}