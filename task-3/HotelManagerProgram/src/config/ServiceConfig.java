package config;

import config.annotations.ConfigProperty;
import config.annotations.PropertyType;

public class ServiceConfig {

    @ConfigProperty(propertyName = "service.default.price")
    private double defaultServicePrice = 20.0;

    @ConfigProperty(propertyName = "service.categories", type = PropertyType.ARRAY)
    private String[] serviceCategories;

    @ConfigProperty(propertyName = "service.max.per.guest")
    private int maxServicesPerGuest = 5;

    @ConfigProperty(propertyName = "service.auto.add.to.bill")
    private boolean autoAddToBill = true;

    public ServiceConfig() {
        if (serviceCategories == null) {
            serviceCategories = new String[]{"Food", "Spa", "Transport", "Laundry", "Entertainment"};
        }
        ConfigLoader.loadConfig(this, "service.properties");
    }

    public double getDefaultServicePrice() {
        return defaultServicePrice;
    }

    public String[] getServiceCategories() {
        return serviceCategories.clone();
    }

    public int getMaxServicesPerGuest() {
        return maxServicesPerGuest;
    }

    public boolean isAutoAddToBill() {
        return autoAddToBill;
    }
}