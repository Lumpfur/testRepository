package controller;

import model.entity.Service;
import service.HotelAdmin;

import java.util.List;
import java.util.Scanner;

public class ServiceController extends BaseController {
    private final HotelAdmin hotelAdmin;

    public ServiceController(Scanner scanner, HotelAdmin hotelAdmin) {
        super(scanner);
        this.hotelAdmin = hotelAdmin;
    }

    public void showMenu() {
        String[] options = {
                "Add service",
                "Find service by ID",
                "Show all services",
                "Update service data",
                "Change service price"
        };

        boolean running = true;
        while (running) {
            printMenu("Service Management", options);
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addService();
                    break;
                case 2:
                    findServiceById();
                    break;
                case 3:
                    getAllServices();
                    break;
                case 4:
                    updateService();
                    break;
                case 5:
                    changeServicePrice();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void addService() {
        System.out.println("\n--- Add Service ---");
        String id = getStringInput("Enter service ID: ");
        String name = getStringInput("Enter service name: ");
        double price = getDoubleInput("Enter price: ");
        String description = getStringInput("Enter description: ");

        Service service = new Service(id, name, price, description);
        boolean success = hotelAdmin.addService(service);
        if (success) {
            System.out.println("Service added successfully!");
        } else {
            System.out.println("Failed to add service!");
        }
    }

    private void findServiceById() {
        System.out.println("\n--- Find Service by ID ---");
        String id = getStringInput("Enter service ID: ");

        Service foundService = hotelAdmin.getServiceManager().getService(id);
        if (foundService != null) {
            System.out.println("Found service: " + foundService);
        } else {
            System.out.println("Service not found!");
        }
    }

    private void getAllServices() {
        System.out.println("\n--- All Services ---");
        List<Service> services = hotelAdmin.getServiceManager().getAllServices();
        if (services.isEmpty()) {
            System.out.println("Service list is empty!");
        } else {
            services.forEach(System.out::println);
        }
    }

    private void updateService() {
        System.out.println("\n--- Update Service ---");
        String id = getStringInput("Enter service ID to update: ");

        Service serviceToUpdate = hotelAdmin.getServiceManager().getService(id);
        if (serviceToUpdate != null) {

            double price = getDoubleInput("Enter new price: ");
            String description = getStringInput("Enter new description: ");

            serviceToUpdate.setPrice(price);
            serviceToUpdate.setDescription(description);

            System.out.println("Service data updated!");
        } else {
            System.out.println("Service not found!");
        }
    }

    private void changeServicePrice() {
        System.out.println("\n--- Change Service Price ---");
        String id = getStringInput("Enter service ID: ");
        double newPrice = getDoubleInput("Enter new price: ");

        boolean success = hotelAdmin.changeServicePrice(id, newPrice);
        if (success) {
            System.out.println("Service price changed successfully!");
        } else {
            System.out.println("Failed to change service price!");
        }
    }
}