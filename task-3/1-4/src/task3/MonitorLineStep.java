package task3;

public class MonitorLineStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("step 3: ");
        System.out.println("creating the display...");
        return new Monitor();
    }
}
