package service;

import model.entity.*;
import model.enums.RoomStatus;
import exception.ImportException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CSVImporter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER_SHORT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Import guests from CSV
    public static List<Guest> importGuests(String filePath, HotelAdmin hotelAdmin) {
        List<Guest> importedGuests = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                try {
                    String[] data = line.split(",");
                    String id = data[0].trim();
                    String name = data[1].trim();
                    String phone = data[2].trim();
                    String email = data[3].trim();

                    Guest guest = new Guest(id, name, phone, email);
                    importedGuests.add(guest);
                    hotelAdmin.addGuest(guest);

                } catch (Exception e) {
                    throw new ImportException("Error parsing guest data: " + line, e);
                }
            }
        } catch (IOException e) {
            throw new ImportException("Failed to import guests from CSV: " + e.getMessage(), e);
        }

        return importedGuests;
    }

    // Import rooms with occupancy and services
    public static List<Room> importRooms(String filePath, HotelAdmin hotelAdmin) {
        List<Room> importedRooms = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                try {
                    String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                    // Basic room data - исправлено для вашей структуры Room
                    String roomNumber = data[0].trim();
                    String type = data[1].trim();
                    double price = Double.parseDouble(data[2].trim());
                    int capacity = data.length > 3 ? Integer.parseInt(data[3].trim()) : 2;
                    int stars = data.length > 4 ? Integer.parseInt(data[4].trim()) : 3;

                    Room room = new Room(roomNumber, type, price, capacity, stars);

                    // Handle occupancy
                    if (data.length > 5) {
                        String statusStr = data[5].trim();
                        RoomStatus status = RoomStatus.valueOf(statusStr.toUpperCase());
                        room.setStatus(status);
                    }

                    // Handle guests if occupied
                    if (room.getStatus() == RoomStatus.OCCUPIED && data.length > 6 && !data[6].trim().isEmpty()) {
                        String guestsInfo = data[6].trim().replace("\"", "");
                        processRoomGuests(room, guestsInfo, hotelAdmin);
                    }

                    // Handle services if exist
                    if (data.length > 7 && !data[7].trim().isEmpty()) {
                        String servicesInfo = data[7].trim().replace("\"", "");
                        processRoomServices(room, servicesInfo);
                    }

                    importedRooms.add(room);
                    hotelAdmin.addRoom(room);

                } catch (Exception e) {
                    throw new ImportException("Error parsing room data: " + line, e);
                }
            }
        } catch (IOException e) {
            throw new ImportException("Failed to import rooms from CSV: " + e.getMessage(), e);
        }

        return importedRooms;
    }

    // Import services from CSV
    public static List<Service> importServices(String filePath, HotelAdmin hotelAdmin) {
        List<Service> importedServices = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                try {
                    String[] data = line.split(",");
                    String id = data[0].trim();
                    String name = data[1].trim();
                    double price = Double.parseDouble(data[2].trim());
                    String description = data.length > 3 ? data[3].trim() : "";

                    Service service = new Service(id, name, price, description);
                    importedServices.add(service);
                    hotelAdmin.addService(service);

                } catch (Exception e) {
                    throw new ImportException("Error parsing service data: " + line, e);
                }
            }
        } catch (IOException e) {
            throw new ImportException("Failed to import services from CSV: " + e.getMessage(), e);
        }

        return importedServices;
    }

    private static void processRoomGuests(Room room, String guestsInfo, HotelAdmin hotelAdmin) {
        try {
            String[] guestEntries = guestsInfo.split(";");
            for (String guestEntry : guestEntries) {
                if (!guestEntry.trim().isEmpty()) {
                    String[] guestData = guestEntry.trim().split("\\|");
                    String guestId = guestData[0].trim();

                    // Find guest in hotel admin or create new one
                    Guest guest = hotelAdmin.getGuestManager().getGuest(guestId);
                    if (guest == null) {
                        // Create temporary guest if not found
                        String name = guestData.length > 1 ? guestData[1].trim() : "Unknown";
                        String phone = guestData.length > 2 ? guestData[2].trim() : "";
                        String email = guestData.length > 3 ? guestData[3].trim() : "";
                        guest = new Guest(guestId, name, phone, email);
                        hotelAdmin.addGuest(guest);
                    }

                    room.checkIn(guest);
                }
            }
        } catch (Exception e) {
            throw new ImportException("Invalid guests data format: " + guestsInfo, e);
        }
    }

    private static void processRoomServices(Room room, String servicesInfo) {
        try {
            String[] serviceEntries = servicesInfo.split(";");
            for (String serviceEntry : serviceEntries) {
                if (!serviceEntry.trim().isEmpty()) {
                    String[] serviceData = serviceEntry.trim().split("\\|");
                    String serviceId = serviceData[0].trim();
                    String serviceName = serviceData.length > 1 ? serviceData[1].trim() : "Unknown Service";
                    double servicePrice = serviceData.length > 2 ? Double.parseDouble(serviceData[2].trim()) : 0.0;
                    String description = serviceData.length > 3 ? serviceData[3].trim() : "";

                    Service service = new Service(serviceId, serviceName, servicePrice, description);
                    room.addService(service);
                }
            }
        } catch (Exception e) {
            throw new ImportException("Invalid services data format: " + servicesInfo, e);
        }
    }
}