package task2;
import java.util.List;
import java.util.ArrayList;

public class Bouquet {
    private List<Flowers> flowers;

    public Bouquet() {
        this.flowers = new ArrayList<>();
    }

    public void addFlower(Flowers flower) {
        flowers.add(flower);
    }

    public double calculateCost() {
        double total = 0.0;
        for (Flowers flower: flowers) {
            total += flower.getTotalCost();
        }
        return total;
    }

    public void display() {
        System.out.println("description: ");

        for(Flowers flower: flowers) {
            System.out.println(flower.getDescription());
        }
    }
}
