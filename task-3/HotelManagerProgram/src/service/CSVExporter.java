package service;

import model.entity.Guest;
import model.entity.Room;
import model.entity.Service;
import exception.ExportException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    public static void exportGuests(List<Guest> guests, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID,Name,Phone,Email\n");

            for (Guest guest : guests) {
                writer.write(String.format("%s,%s,%s,%s\n",
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
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Number,Type,Price,Capacity,Stars,Status\n");

            for (Room room : rooms) {
                writer.write(String.format("%s,%s,%.2f,%d,%d,%s\n",
                        escapeCsv(room.getNumber()),
                        escapeCsv(room.getType()),
                        room.getPrice(),
                        room.getCapacity(),
                        room.getStars(),
                        escapeCsv(room.getStatus().name())));
            }
        } catch (IOException e) {
            throw new ExportException("Failed to export rooms to CSV: " + e.getMessage(), e);
        }
    }

    public static void exportServices(List<Service> services, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID,Name,Price,Description\n");

            for (Service service : services) {
                writer.write(String.format("%s,%s,%.2f,%s\n",
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