package test;

import controller.MainController;

public class Main {
    public static void main(String[] args) {
        try {
            MainController mainController = new MainController();
            mainController.start();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}