package service;

import model.entity.Guest;
import model.entity.Room;
import model.entity.Service;
import model.enums.RoomStatus;
import exception.ImportException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVImporter {

    public static List<Guest> importGuests(String filePath, HotelAdmin hotelAdmin) {
        List<Guest> importedGuests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Пропускаем заголовок

            while ((line = reader.readLine()) != null) {
                try {
                    String[] fields = parseCsvLine(line);
                    if (fields.length >= 4) {
                        String id = fields[0];
                        String name = fields[1];
                        String phone = fields[2];
                        String email = fields[3];

                        Guest guest = new Guest(id, name, phone, email);
                        importedGuests.add(guest);


                        Guest existingGuest = hotelAdmin.getGuestManager().getGuest(id);
                        if (existingGuest != null) {
                            existingGuest.setName(name);
                            existingGuest.setPhone(phone);
                            existingGuest.setEmail(email);
                        } else {
                            hotelAdmin.addGuest(guest);
                        }
                    }
                } catch (Exception e) {
                    throw new ImportException("Error parsing guest data: " + line, e);
                }
            }
            return importedGuests;
        } catch (IOException e) {
            throw new ImportException("Failed to import guests from CSV: " + e.getMessage(), e);
        }
    }

    public static List<Room> importRooms(String filePath, HotelAdmin hotelAdmin) {
        List<Room> importedRooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); //skip the title

            while ((line = reader.readLine()) != null) {
                try {
                    String[] fields = parseCsvLine(line);
                    if (fields.length >= 6) {
                        String number = fields[0];
                        String type = fields[1];
                        double price = Double.parseDouble(fields[2]);
                        int capacity = Integer.parseInt(fields[3]);
                        int stars = Integer.parseInt(fields[4]);
                        RoomStatus status = RoomStatus.valueOf(fields[5]);

                        Room room = new Room(number, type, price, capacity, stars);
                        room.setStatus(status);
                        importedRooms.add(room);

                        Room existingRoom = hotelAdmin.getRoomManager().getRoom(number);
                        if (existingRoom != null) {
                            existingRoom.setType(type);
                            existingRoom.setPrice(price);
                            existingRoom.setCapacity(capacity);
                            existingRoom.setStars(stars);
                            existingRoom.setStatus(status);
                        } else {
                            hotelAdmin.addRoom(room);
                        }
                    }
                } catch (Exception e) {
                    throw new ImportException("Error parsing room data: " + line, e);
                }
            }
            return importedRooms;
        } catch (IOException e) {
            throw new ImportException("Failed to import rooms from CSV: " + e.getMessage(), e);
        }
    }

    public static List<Service> importServices(String filePath, HotelAdmin hotelAdmin) {
        List<Service> importedServices = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                try {
                    String[] fields = parseCsvLine(line);
                    if (fields.length >= 4) {
                        String id = fields[0];
                        String name = fields[1];
                        double price = Double.parseDouble(fields[2]);
                        String description = fields[3];

                        Service service = new Service(id, name, price, description);
                        importedServices.add(service);

                        Service existingService = hotelAdmin.getServiceManager().getService(id);
                        if (existingService != null) {
                            existingService.setPrice(price);
                            existingService.setDescription(description);
                        } else {
                            hotelAdmin.addService(service);
                        }
                    }
                } catch (Exception e) {
                    throw new ImportException("Error parsing service data: " + line, e);
                }
            }
            return importedServices;
        } catch (IOException e) {
            throw new ImportException("Failed to import services from CSV: " + e.getMessage(), e);
        }
    }

    private static String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }
}