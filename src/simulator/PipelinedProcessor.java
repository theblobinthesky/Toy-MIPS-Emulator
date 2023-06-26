package simulator;

public class PipelinedProcessor implements Processor {
    private final InstructionMemory instructionMemory;
    private final RegisterTable registerTable;
    private final ALU alu;
    private final DataMemory dataMemory;
    int programCounter;

    public static class AllControl {
        private boolean jump;
        private boolean regDst;
        private boolean branch;
        private boolean memRead;
        private boolean memToReg;
        private boolean memWrite;
        private ALU.ALUOperation aluOp;
        private boolean aluSrc;
        private boolean regWrite;
        private boolean aluAddImmediate;

        protected static AllControl getRTypePreset() {
            AllControl control = new AllControl();
            control.regDst = true;
            control.regWrite = true;
            control.aluOp = ALU.ALUOperation.add;

            return control;
        }

        protected static AllControl getITypePreset() {
            AllControl control = new AllControl();
            control.aluOp = ALU.ALUOperation.add;

            return control;
        }

        protected static AllControl getJTypePreset() {
            AllControl control = new AllControl();
            control.jump = true;
            control.aluOp = ALU.ALUOperation.add;

            return control;
        }
    }

    public static class FetchDecodeData {
        Instruction instruction;
    }

    private static class DecodeExecuteData {
        Instruction instruction;
        int registerValue1;
        int registerValue2;
    }

    private static class ExecuteMemoryData {
        Instruction instruction;
        int registerValue2;
        int aluOutput;
        boolean aluZero;
    }

    private static class MemoryWriteBackData {
        Instruction instruction;
        int aluOutput;
        boolean aluZero;
        int memRead;
    }

    private PipelineOutRegisters<AllControl, FetchDecodeData> fetchDecodeRegs;
    private PipelineOutRegisters<AllControl, DecodeExecuteData> decodeExecuteRegs;
    private PipelineOutRegisters<AllControl, ExecuteMemoryData> executeMemoryRegs;
    private PipelineOutRegisters<AllControl, MemoryWriteBackData> memoryWriteBackRegs;

    public PipelinedProcessor() {
        instructionMemory = new InstructionMemory();
        registerTable = new RegisterTable();
        alu = new ALU();
        dataMemory = new DataMemory();
        reset();
    }

    public void reset() {
        AllControl emptyControl = new AllControl();
        emptyControl.aluOp = ALU.ALUOperation.add;

        FetchDecodeData emptyFetchDecodeData = new FetchDecodeData();
        DecodeExecuteData emptyDecodeExecuteData = new DecodeExecuteData();
        ExecuteMemoryData emptyExecuteMemoryData = new ExecuteMemoryData();
        MemoryWriteBackData emptyMemoryWriteBackData = new MemoryWriteBackData();
        Instruction emptyInstruction = new Instruction();

        emptyFetchDecodeData.instruction = emptyInstruction;
        emptyDecodeExecuteData.instruction = emptyInstruction;
        emptyExecuteMemoryData.instruction = emptyInstruction;
        emptyMemoryWriteBackData.instruction = emptyInstruction;

        fetchDecodeRegs = new PipelineOutRegisters<>(emptyControl, emptyFetchDecodeData);
        decodeExecuteRegs = new PipelineOutRegisters<>(emptyControl, emptyDecodeExecuteData);
        executeMemoryRegs = new PipelineOutRegisters<>(emptyControl, emptyExecuteMemoryData);
        memoryWriteBackRegs = new PipelineOutRegisters<>(emptyControl, emptyMemoryWriteBackData);

        instructionMemory.zeroOutEverything();
        registerTable.zeroOutEverything();
        dataMemory.zeroOutEverything();
        programCounter = 0;
    }

    private void doInstructionFetch() {
        AllControl control = null;
        FetchDecodeData data = new FetchDecodeData();
        data.instruction = instructionMemory.fetchInstruction(programCounter);
        assert data.instruction != null;

        switch (data.instruction.type) {
            case RType -> {
                control = AllControl.getRTypePreset();

                if (data.instruction.opcode == Opcode.arithmetic) {
                    switch (data.instruction.funct) {
                        case add -> control.aluOp = ALU.ALUOperation.add;
                        case sub -> control.aluOp = ALU.ALUOperation.sub;
                        case and -> control.aluOp = ALU.ALUOperation.and;
                        case or -> control.aluOp = ALU.ALUOperation.or;
                        case slt -> control.aluOp = ALU.ALUOperation.slt;
                    }
                }
            }
            case IType -> {
                control = AllControl.getITypePreset();

                switch (data.instruction.opcode) {
                    case lw -> {
                        control.aluSrc = true;
                        control.regWrite = true;
                        control.memRead = true;
                        control.memToReg = true;
                    }
                    case sw -> {
                        control.aluSrc = true;
                        control.memWrite = true;
                    }
                    case beq -> {
                        control.branch = true;
                        control.aluOp = ALU.ALUOperation.sub;
                    }
                    case addi -> {
                        control.aluOp = ALU.ALUOperation.add;
                        control.aluAddImmediate = true;
                        control.regWrite = true;
                    }
                }

            }
            case JType -> {
                control = AllControl.getJTypePreset();

                if (data.instruction.opcode == Opcode.j) control.jump = true;
            }
        }

        fetchDecodeRegs.swapInAtTheEndOfCycle(control, data);
    }

    private void doInstructionDecode() {
        AllControl allControl = fetchDecodeRegs.getControlDuringCycle();
        FetchDecodeData fetchDecodeData = fetchDecodeRegs.getDataDuringCycle();

        DecodeExecuteData data = new DecodeExecuteData();
        data.instruction = fetchDecodeData.instruction;

        data.registerValue1 = registerTable.get(fetchDecodeData.instruction.rs);
        data.registerValue2 = registerTable.get(fetchDecodeData.instruction.rt);

        decodeExecuteRegs.swapInAtTheEndOfCycle(allControl, data);
    }

    private void doExecute() {
        AllControl allControl = decodeExecuteRegs.getControlDuringCycle();
        DecodeExecuteData decodeExecuteData = decodeExecuteRegs.getDataDuringCycle();

        ExecuteMemoryData data = new ExecuteMemoryData();
        data.instruction = decodeExecuteData.instruction;
        data.registerValue2 = decodeExecuteData.registerValue2;

        int signExtendedAddress = decodeExecuteData.instruction.immediate;

        int value2 = decodeExecuteData.registerValue2;
        if (allControl.aluSrc) value2 = signExtendedAddress;
        else if (allControl.aluAddImmediate) value2 = decodeExecuteData.instruction.immediate;

        data.aluOutput = alu.calculate(allControl.aluOp, decodeExecuteData.registerValue1, value2);
        data.aluZero = (data.aluOutput == 0);

        executeMemoryRegs.swapInAtTheEndOfCycle(allControl, data);
    }

    private void doMemory() {
        AllControl allControl = executeMemoryRegs.getControlDuringCycle();
        ExecuteMemoryData executeMemoryData = executeMemoryRegs.getDataDuringCycle();

        MemoryWriteBackData data = new MemoryWriteBackData();
        data.instruction = executeMemoryData.instruction;
        data.aluOutput = executeMemoryData.aluOutput;
        data.aluZero = executeMemoryData.aluZero;

        if (allControl.memWrite) {
            dataMemory.write(executeMemoryData.aluOutput, executeMemoryData.registerValue2);
        } else if (allControl.memRead) {
            data.memRead = dataMemory.get(executeMemoryData.aluOutput);
        }

        memoryWriteBackRegs.swapInAtTheEndOfCycle(allControl, data);
    }

    private void doWriteBack() {
        AllControl allControl = memoryWriteBackRegs.getControlDuringCycle();
        MemoryWriteBackData memoryWriteBackData = memoryWriteBackRegs.getDataDuringCycle();

        if (allControl.regWrite) {
            RegisterName writeRegister = allControl.regDst ? memoryWriteBackData.instruction.rd : memoryWriteBackData.instruction.rt;
            int writeValue = allControl.memToReg ? memoryWriteBackData.memRead : memoryWriteBackData.aluOutput;
            registerTable.set(writeRegister, writeValue);
        }
    }

    private void endCycle() {
        fetchDecodeRegs.endCycle();
        decodeExecuteRegs.endCycle();
        executeMemoryRegs.endCycle();
        memoryWriteBackRegs.endCycle();
    }

    private void simulateCycle() {
        doInstructionFetch();
        doInstructionDecode();
        doExecute();
        doMemory();
        doWriteBack();

        endCycle();

        doInstructionDecode();
        doExecute();
        doMemory();
        doWriteBack();

        endCycle();

        doExecute();
        doMemory();
        doWriteBack();

        endCycle();

        doMemory();
        doWriteBack();

        endCycle();

        doWriteBack();

        // Lastly increase the pc.
        AllControl allControl = memoryWriteBackRegs.getControlDuringCycle();
        MemoryWriteBackData memoryWriteBackData = memoryWriteBackRegs.getDataDuringCycle();

        programCounter += 4;

        if (memoryWriteBackData.aluZero && allControl.branch) {
            int offset = memoryWriteBackData.instruction.immediate << 2;
            programCounter += offset;
        }

        if (allControl.jump) {
            int address = memoryWriteBackData.instruction.pseudoAddress << 2;
            programCounter = (programCounter & 0xF0000000) | address;
        }

        endCycle();
    }

    public void simulate() {
        while (instructionMemory.hasNextInstruction(programCounter)) {
            simulateCycle();
        }
    }

    public int getRegisterValue(RegisterName r) {
        return registerTable.get(r);
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public InstructionMemory getInstructionMemory() {
        return instructionMemory;
    }
}