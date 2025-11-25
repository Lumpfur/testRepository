package service;

import model.entity.Guest;
import model.entity.Room;
import model.entity.Service;
import model.enums.RoomStatus;
import exception.ExportException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CSVExporter {

    public static void exportGuests(List<Guest> guests, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("ID,Name,Phone,Email");

            for (Guest guest : guests) {
                writer.println(String.format("%s,%s,%s,%s",
                        escapeCsv(guest.getId()),
                        escapeCsv(guest.getName()),
                        escapeCsv(guest.getPhone()),
                        escapeCsv(guest.getEmail())));
            }
        } catch (IOException e) {
            throw new ExportException("Failed to export guests to CSV: " + e.getMessage(), e);
        }
    }

    public static void exportRooms(List<Room> rooms, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("Number,Type,Price,Capacity,Stars,Status,CurrentGuests,GuestServices");

            for (Room room : rooms) {
                StringBuilder line = new StringBuilder();

                // Basic room info
                line.append(escapeCsv(room.getNumber())).append(",");
                line.append(escapeCsv(room.getType())).append(",");
                line.append(room.getPrice()).append(",");
                line.append(room.getCapacity()).append(",");
                line.append(room.getStars()).append(",");
                line.append(room.getStatus().name()).append(",");

                // Current guests info
                List<Guest> currentGuests = room.getCurrentGuests();
                if (!currentGuests.isEmpty()) {
                    StringBuilder guestsInfo = new StringBuilder();
                    for (int i = 0; i < currentGuests.size(); i++) {
                        Guest guest = currentGuests.get(i);
                        guestsInfo.append(guest.getId()).append("|")
                                .append(guest.getName()).append("|")
                                .append(guest.getPhone()).append("|")
                                .append(guest.getEmail());
                        if (i < currentGuests.size() - 1) {
                            guestsInfo.append("; ");
                        }
                    }
                    line.append("\"").append(guestsInfo).append("\"").append(",");
                } else {
                    line.append(",");
                }

                // Guest services info
                List<Service> guestServices = room.getGuestServices();
                if (!guestServices.isEmpty()) {
                    StringBuilder servicesInfo = new StringBuilder();
                    for (int i = 0; i < guestServices.size(); i++) {
                        Service service = guestServices.get(i);
                        servicesInfo.append(service.getId()).append("|")
                                .append(service.getName()).append("|")
                                .append(service.getPrice()).append("|")
                                .append(service.getDescription());
                        if (i < guestServices.size() - 1) {
                            servicesInfo.append("; ");
                        }
                    }
                    line.append("\"").append(servicesInfo).append("\"");
                }

                writer.println(line.toString());
            }
        } catch (IOException e) {
            throw new ExportException("Failed to export rooms to CSV: " + e.getMessage(), e);
        }
    }

    public static void exportServices(List<Service> services, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("ID,Name,Price,Description");

            for (Service service : services) {
                writer.println(String.format("%s,%s,%.2f,%s",
                        escapeCsv(service.getId()),
                        escapeCsv(service.getName()),
                        service.getPrice(),
                        escapeCsv(service.getDescription())));
            }
        } catch (IOException e) {
            throw new ExportException("Failed to export services to CSV: " + e.getMessage(), e);
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}