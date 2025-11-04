package task2;

public class Main {
    public static void main(String[] args) {
        Bouquet bouquet = new Bouquet();

        bouquet.addFlower(new Rose("red", 5, 150, true));
        bouquet.addFlower(new Tulip("blue", 7, 170, "basic"));
        bouquet.addFlower(new Lily("pink", 5, 210, 4));

        bouquet.display();
        System.out.println("whole cost: " + bouquet.calculateCost());

    }


}
