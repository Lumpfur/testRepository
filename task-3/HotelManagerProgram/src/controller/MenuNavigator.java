package controller;

import UI.*;
import java.util.Scanner;

public class MenuNavigator {
    private Menu currentMenu;
    private Scanner scanner;

    public MenuNavigator(Menu rootMenu) {
        this.currentMenu = rootMenu;
        this.scanner = new Scanner(System.in);
    }

    public void navigate() {
        while (currentMenu != null) {
            // Display menu
            displayMenu(currentMenu);

            System.out.print("Select option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                if (choice == 0) {
                    // Exit or go back
                    System.out.println("Goodbye buddie!");
                    break;
                } else if (choice > 0 && choice <= currentMenu.getItems().size()) {
                    MenuItem selectedItem = currentMenu.getItems().get(choice - 1);
                    selectedItem.getAction().execute();
                } else {
                    System.out.println("Invalid option! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // clear invalid input
            }
        }
        scanner.close();
    }

    private void displayMenu(Menu menu) {
        System.out.println("\n" + "=".repeat(50));
        if (menu.getHeader() != null && !menu.getHeader().isEmpty()) {
            System.out.println(menu.getHeader());
        }
        System.out.println(menu.getTitle());
        System.out.println("-".repeat(50));

        int index = 1;
        for (MenuItem item : menu.getItems()) {
            System.out.println(index + ". " + item.getName());
            index++;
        }
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
    }

    public void setCurrentMenu(Menu menu) {
        this.currentMenu = menu;
    }
}