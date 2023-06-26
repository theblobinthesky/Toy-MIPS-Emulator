package simulator;

public class PipelineOutRegisters<Control, Data> {
    protected Control controlEmpty, controlIn, controlOut;
    protected Data dataEmpty, dataIn, dataOut;

    public PipelineOutRegisters(Control emptyControl, Data emptyData) {
        controlEmpty = controlIn = controlOut = emptyControl;
        dataEmpty = dataIn = dataOut = emptyData;
    }

    public void swapInAtTheEndOfCycle(Control controlIn, Data dataIn) {
        this.controlIn = controlIn;
        this.dataIn = dataIn;
    }

    public void endCycle() {
        this.controlOut = controlIn;
        this.dataOut = dataIn;

        controlIn = controlEmpty;
        dataIn = dataEmpty;
    }

    public Control getControlDuringCycle() { return controlOut; }
    public Data getDataDuringCycle() { return dataOut; }
}
