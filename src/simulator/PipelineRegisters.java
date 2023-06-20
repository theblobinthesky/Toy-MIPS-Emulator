package simulator;

public class PipelineRegisters<Control extends PipelineControl, Data extends PipelineData> {
    protected Control controlIn, controlOut;
    protected Data dataIn, dataOut;

    public PipelineRegisters(Control control, Data data) {
        this.controlIn = control;
        controlIn = null;

        this.dataIn = data;
        dataOut = null;
    }

    public void notifyCycle() {
        controlOut = controlIn;
        dataOut = dataIn;

        controlIn = null;
        dataIn = null;
    }
}
