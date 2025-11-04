package task3;

public class LaptopAssemblyLine implements IAssemblyLine {
    private ILineStep caseStep;
    private ILineStep motherboardStep;
    private ILineStep monitorStep;

    //constructor takes 3 steps of creating
    public LaptopAssemblyLine(ILineStep caseStep, ILineStep motherboardStep, ILineStep monitorStep) {
        this.caseStep = caseStep;
        this.motherboardStep = motherboardStep;
        this.monitorStep = monitorStep;
    }

    @Override
    public IProduct assembleProduct(IProduct product) {

        System.out.println("the progress of creating the laptop has started...");

        //System.out.println("step 1");
        IProductPart casePart = caseStep.buildProductPart();
        product.installFirstPart(casePart);

        //System.out.println("step 2");
        IProductPart motherboardPart = motherboardStep.buildProductPart();
        product.installSecondPart(motherboardPart);

        //System.out.println("step 3");
        IProductPart monitorPart = monitorStep.buildProductPart();
        product.installThirdPart(monitorPart);

        return product;
    }
}
