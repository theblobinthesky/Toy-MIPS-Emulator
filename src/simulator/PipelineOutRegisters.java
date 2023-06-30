package simulator;

public class PipelineOutRegisters<Control, Data> {
    protected Control controlEmpty, controlIn, controlOut;
    protected Data dataEmpty, dataIn, dataOut;
    boolean keep;

    public PipelineOutRegisters(Control emptyControl, Data emptyData) {
        controlEmpty = controlIn = controlOut = emptyControl;
        dataEmpty = dataIn = dataOut = emptyData;
    }

    public void swapInAtTheEndOfCycle(Control controlIn, Data dataIn) {
        this.controlIn = controlIn;
        this.dataIn = dataIn;
    }

    public void keepAtTheEndOfCycle() {
        controlIn = controlOut;
        dataIn = dataOut;
    }

    public void zeroAtTheEndOfCycle() {
        controlIn = controlEmpty;
    }

    public void endCycle() {
        controlOut = controlIn;
        dataOut = dataIn;

        controlIn = controlEmpty;
        dataIn = dataEmpty;
    }

    public Control getControlDuringCycle() { return controlOut; }
    public Data getDataDuringCycle() { return dataOut; }
}
