package UI;

import UI.MenuAction;

public class MenuItem {
    private int number;
    private String name;
    private MenuAction action;

    public MenuItem(String name, MenuAction action) {
        this.name = name;
        this.action = action;
    }

    public MenuItem(int number, String name, MenuAction action) {
        this.number = number;
        this.name = name;
        this.action = action;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public MenuAction getAction() {
        return action;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}