package task2;

class Tulip extends Flowers {
    private String color;
    private String variety;

    public Tulip(String color, int count, double costPerOne, String variety) {
        super("Tulip", count, costPerOne);
        this.color = color;
        this.variety = variety;
    }

    @Override
    public String getDescription() {
        return color + " tulip (" + variety + ")";
    }
}
