package UI;

import model.enums.MenuType;
import UI.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private String title;
    private MenuType type;
    private List<MenuItem> items;
    private String header;
    private String footer;

    public Menu(String title, MenuType type) {
        this.title = title;
        this.type = type;
        this.items = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getTitle() {
        return title;
    }

    public MenuType getType() {
        return type;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public String getHeader() {
        return header;
    }

    public String getFooter() {
        return footer;
    }
}