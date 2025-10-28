package task4;

import java.util.*;

class ServiceManager {
    private Map<String, Service> services;

    public ServiceManager() {
        this.services = new HashMap<>();
    }

    public boolean addService(Service service) {
        if (service != null && !services.containsKey(service.getId())) {
            services.put(service.getId(), service);
            return true;
        }
        return false;
    }

    public boolean removeService(String serviceId) {
        return services.remove(serviceId) != null;
    }

    public boolean changeServicePrice(String serviceId, double newPrice) {
        Service service = services.get(serviceId);
        if (service != null) {
            service.setPrice(newPrice);
            return true;
        }
        return false;
    }

    public Service getService(String serviceId) {
        return services.get(serviceId);
    }

    public List<Service> getAllServices() {
        return new ArrayList<>(services.values());
    }
}