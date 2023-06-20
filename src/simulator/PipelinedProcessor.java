package simulator;

public class PipelinedProcessor {
    private final InstructionMemory instructionMemory;
    private final RegisterTable registerTable;
    private final DataMemory dataMemory;
    private final PipelineControl control;

    private final PipelineRegisters<PipelineControl.InstructionFetch, PipelineData.InstructionFetch> fetchDecodeRegisters;
    private final PipelineRegisters<PipelineControl.InstructionDecode, PipelineData.InstructionDecode> decodeExecuteRegisters;
    private final PipelineRegisters<PipelineControl.Execute, PipelineData.Execute> executeMemoryRegisters;
    private final PipelineRegisters<PipelineControl.Memory, PipelineData.Memory> memoryWriteBackRegisters;

    public PipelinedProcessor() {
        instructionMemory = new InstructionMemory();
        registerTable = new RegisterTable();
        dataMemory = new DataMemory();
        control = null;

        fetchDecodeRegisters = new PipelineRegisters<>(new PipelineControl.InstructionFetch(), new PipelineData.InstructionFetch());
        decodeExecuteRegisters = new PipelineRegisters<>(new PipelineControl.InstructionDecode(), new PipelineData.InstructionDecode());
        executeMemoryRegisters = new PipelineRegisters<>(new PipelineControl.Execute(), new PipelineData.Execute());
        memoryWriteBackRegisters = new PipelineRegisters<>(new PipelineControl.Memory(), new PipelineData.Memory());
    }

    public void reset() {
        instructionMemory.zeroOutEverything();
        registerTable.zeroOutEverything();
        dataMemory.zeroOutEverything();
    }

    private void doInstructionFetch() {
        Instruction instruction = instructionMemory.fetchInstruction(0);

        // missing: control.
        fetchDecodeRegisters.dataIn.instruction = instruction;
    }

    private void doInstructionDecode() {

    }

    private void doExecute() {

    }

    private void doMemory() {

    }

    private void doWriteBack() {

    }

    private void simulateCycle() {
        // Swap all registers.
        fetchDecodeRegisters.notifyCycle();
        decodeExecuteRegisters.notifyCycle();
        executeMemoryRegisters.notifyCycle();
        memoryWriteBackRegisters.notifyCycle();

        // Instruction fetch
        doInstructionFetch();

        // Instruction decode.
        doInstructionDecode();

        // Execute.
        doExecute();

        // Memory.
        doMemory();

        // Write back.
        doWriteBack();
    }

    public void simulate() {
        while(instructionMemory.hasNextInstruction(0)) {
            simulateCycle();
        }
    }

    public int getRegisterValue(RegisterName r) { return registerTable.get(r); }
}