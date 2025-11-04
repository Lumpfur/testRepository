package task3;

public class AssemblyLineTest {
    public static void main(String[] args) {

        ILineStep caseStep = new CaseLineStep();
        ILineStep motherboardStep = new MotherboardLineStep();
        ILineStep monitorStep = new MonitorLineStep();

        IAssemblyLine assemblyLine = new LaptopAssemblyLine(caseStep, motherboardStep, monitorStep);

        IProduct laptop = new Laptop();
        IProduct assembledLaptop = assemblyLine.assembleProduct(laptop);

        if (assembledLaptop instanceof Laptop) {
            ((Laptop) assembledLaptop).displaySpecifications();
        }

    }
}
