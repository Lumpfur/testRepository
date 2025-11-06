package UI;

import model.enums.MenuType;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for creating menu structures with fluent interface
 */
public class MenuBuilder {
    private List<MenuItem> items;
    private String title;
    private MenuType type;
    private String header;
    private String footer;

    public MenuBuilder(MenuType type) {
        this.type = type;
        this.items = new ArrayList<>();
        this.title = type.name(); // Исправлено: используем name() вместо getTitle()
        this.header = "";
        this.footer = "";
    }

    public MenuBuilder(String title, MenuType type) {
        this.type = type;
        this.items = new ArrayList<>();
        this.title = title;
        this.header = "";
        this.footer = "";
    }

    // Add menu item with name and action
    public MenuBuilder addItem(String name, MenuAction action) {
        items.add(new MenuItem(name, action));
        return this;
    }

    // Add menu item with custom number
    public MenuBuilder addItem(int number, String name, MenuAction action) {
        items.add(new MenuItem(number, name, action));
        return this;
    }

    // Set menu title
    public MenuBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    // Set header text displayed above menu
    public MenuBuilder setHeader(String header) {
        this.header = header;
        return this;
    }

    // Set footer text displayed below menu
    public MenuBuilder setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    // Build and return the final Menu object
    public Menu build() {
        Menu menu = new Menu(title, type);
        // Добавляем header и footer через методы Menu если они есть
        // или устанавливаем напрямую если есть поля
        for (MenuItem item : items) {
            menu.addItem(item);
        }
        return menu;
    }

    // === FACTORY METHODS FOR COMMON MENUS ===

    // Build main navigation menu
    public static Menu buildMainMenu(MenuAction guestActions, MenuAction roomActions,
                                     MenuAction serviceActions, MenuAction reportActions,
                                     MenuAction exitAction) {
        return new MenuBuilder(MenuType.MAIN) // Исправлено: MenuType.MAIN вместо MenuType. Main
                .setHeader("=== HOTEL MANAGEMENT SYSTEM ===")
                .addItem(1, "Guest Management", guestActions)
                .addItem(2, "Room Management", roomActions)
                .addItem(3, "Service Management", serviceActions)
                .addItem(4, "Reports & Analytics", reportActions)
                .addItem(0, "Exit System", exitAction)
                .build();
    }

    // Build guest management menu
    public static Menu buildGuestMenu(MenuAction addGuest, MenuAction viewGuests,
                                      MenuAction checkIn, MenuAction checkOut,
                                      MenuAction backAction) {
        return new MenuBuilder(MenuType.GUEST)
                .setHeader("=== GUEST MANAGEMENT ===")
                .addItem(1, "Add New Guest", addGuest)
                .addItem(2, "View All Guests", viewGuests)
                .addItem(3, "Check-in Guest", checkIn)
                .addItem(4, "Check-out Guest", checkOut)
                .addItem(0, "Back to Main Menu", backAction)
                .build();
    }

    // Build room management menu
    public static Menu buildRoomMenu(MenuAction addRoom, MenuAction viewRooms,
                                     MenuAction availableRooms, MenuAction roomDetails,
                                     MenuAction backAction) {
        return new MenuBuilder(MenuType.ROOM)
                .setHeader("=== ROOM MANAGEMENT ===")
                .addItem(1, "Add New Room", addRoom)
                .addItem(2, "View All Rooms", viewRooms)
                .addItem(3, "Available Rooms", availableRooms)
                .addItem(4, "Room Details", roomDetails)
                .addItem(0, "Back to Main Menu", backAction)
                .build();
    }

    // Build service management menu
    public static Menu buildServiceMenu(MenuAction addService, MenuAction viewServices,
                                        MenuAction assignService, MenuAction backAction) {
        return new MenuBuilder(MenuType.SERVICE)
                .setHeader("=== SERVICE MANAGEMENT ===")
                .addItem(1, "Add New Service", addService)
                .addItem(2, "View All Services", viewServices)
                .addItem(3, "Assign Service to Guest", assignService)
                .addItem(0, "Back to Main Menu", backAction)
                .build();
    }

    // Build confirmation dialog (Yes/No)
    public static Menu buildConfirmationMenu(String message, MenuAction confirmAction,
                                             MenuAction cancelAction) {
        return new MenuBuilder("Confirmation", MenuType.SUBMENU)
                .setHeader("=== CONFIRMATION ===")
                .setFooter(message)
                .addItem(1, "Yes, Confirm", confirmAction)
                .addItem(0, "No, Cancel", cancelAction)
                .build();
    }
}