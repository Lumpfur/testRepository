package task2;

class Rose extends Flowers {
    private String color;
    private boolean hasThorns;

    public Rose(String color, int count, double costPerOne, boolean hasThorns) {
        super("Rose", count, costPerOne);
        this.color = color;
        this.hasThorns = hasThorns;
    }

    @Override
    public String getDescription() {
        return color + " rose" + (hasThorns ? " with thorns" : " without thorns");
    }
}
