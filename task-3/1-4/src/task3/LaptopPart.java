package task3;

public abstract class LaptopPart implements IProductPart {
    protected String name;
    protected String description;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " (" + description + ")";
    }
}
