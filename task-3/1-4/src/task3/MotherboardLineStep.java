package task3;

public class MotherboardLineStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("step 2: ");
        System.out.println("creating the mother board...");
        return new Motherboard();
    }
}
