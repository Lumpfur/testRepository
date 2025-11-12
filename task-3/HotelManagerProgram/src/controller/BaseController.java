package controller;

import java.util.Scanner;

public abstract class BaseController {
    protected Scanner scanner;

    public BaseController(Scanner scanner) {
        this.scanner = scanner;
    }

    protected void printMenu(String title, String[] options) {
        System.out.println("\n=== " + title + " ===");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Back");
        System.out.print("Choose option: ");
    }

    protected int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    protected int getIntInput(String prompt) {
        System.out.print(prompt);
        return getIntInput();
    }

    protected String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    protected double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Please enter a number: ");
            scanner.next();
        }
        double input = scanner.nextDouble();
        scanner.nextLine();
        return input;
    }

    protected boolean getBooleanInput(String prompt) {
        System.out.print(prompt + " (y/n): ");
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes");
    }
}