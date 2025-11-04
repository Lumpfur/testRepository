package task2;


abstract class Flowers {
    private String type;
    private int count;
    private double costPerOne;

    public Flowers(String type, int count, double costPerOne) {
        this.type = type;
        this.count = count;
        this.costPerOne = costPerOne;
    }

    double getCostPerOne() {
        return costPerOne;
    }

    String getType() {
        return type;
    }

    int getCount() {
        return count;
    }

    public double getTotalCost() {
        return count * costPerOne;
    }

    public abstract String getDescription();

}
