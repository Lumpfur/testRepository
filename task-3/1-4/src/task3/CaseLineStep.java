package task3;

public class CaseLineStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("step 1: ");
        System.out.println("creating the body...");
        return new LaptopCase();
    }
}
