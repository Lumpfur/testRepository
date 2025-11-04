package task2;

class Lily extends Flowers {
    private String color;
    private int budSize;

    public Lily(String color, int count, double costPerOne, int budSize) {
        super("Lily", count, costPerOne);
        this.color = color;
        this.budSize = budSize;
    }

    @Override
    public String getDescription() {
        return color + " lily with bud " + budSize + "sm";
    }
}
