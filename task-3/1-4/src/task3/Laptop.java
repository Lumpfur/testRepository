package task3;

public class Laptop implements IProduct {
    private IProductPart casePart;
    private IProductPart motherboardPart;
    private IProductPart monitorPart;

    @Override
    public void installFirstPart(IProductPart part) {
        this.casePart = part;
        System.out.println("installed " + part);
    }

    @Override
    public void installSecondPart(IProductPart part) {
        this.motherboardPart = part;
        System.out.println("installed " + part);
    }

    @Override
    public void installThirdPart(IProductPart part) {
        this.monitorPart = part;
        System.out.println("installed " + part);
    }

    public void displaySpecifications() {
        System.out.println("Description of the laptop");
        System.out.println("Body " + (casePart != null ? casePart.getDescription() : "null"));
        System.out.println("Mother Board: " + (motherboardPart != null ? motherboardPart.getDescription() : "null"));
        System.out.println("Monitor: " + (monitorPart != null ? monitorPart.getDescription() : "null"));
        System.out.println("Status: " + (isComplete() ? "Completed" : "in progress"));
    }

    public boolean isComplete() {
        return casePart != null && motherboardPart != null && monitorPart != null;
    }
}
