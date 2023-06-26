package simulator;

public class NaiveProcessor implements Processor {
    private final InstructionMemory instructionMemory;
    private final RegisterTable registerTable;
    private final ALU alu;
    private final DataMemory dataMemory;

    int programCounter;
    private Instruction instruction;
    private Control control;
    private Data data;

    private static class Control {
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

        protected static Control getRTypePreset() {
            Control control = new Control();
            control.regDst = true;
            control.regWrite = true;
            control.aluOp = ALU.ALUOperation.add;

            return control;
        }

        protected static Control getITypePreset() {
            Control control = new Control();
            control.aluOp = ALU.ALUOperation.add;

            return control;
        }

        protected static Control getJTypePreset() {
            Control control = new Control();
            control.jump = true;
            control.aluOp = ALU.ALUOperation.add;

            return control;
        }
    }

    public static class Data {
        int registerValue1;
        int registerValue2;
        int aluOutput;
        boolean aluZero;
        int memRead;
    }

    public NaiveProcessor() {
        instructionMemory = new InstructionMemory();
        registerTable = new RegisterTable();
        alu = new ALU();
        dataMemory = new DataMemory();
        control = new Control();
        data = new Data();
        reset();
    }

    public void reset() {
        instructionMemory.zeroOutEverything();
        registerTable.zeroOutEverything();
        dataMemory.zeroOutEverything();
        programCounter = 0;
    }

    private void doInstructionFetch() {
        instruction = instructionMemory.fetchInstruction(programCounter);
        assert instruction != null;

        switch(instruction.type) {
            case RType -> {
                control = Control.getRTypePreset();

                if(instruction.opcode == Opcode.arithmetic) {
                    switch (instruction.funct) {
                        case add -> control.aluOp = ALU.ALUOperation.add;
                        case sub -> control.aluOp = ALU.ALUOperation.sub;
                        case and -> control.aluOp = ALU.ALUOperation.and;
                        case or -> control.aluOp = ALU.ALUOperation.or;
                        case slt -> control.aluOp = ALU.ALUOperation.slt;
                    }
                }
            }
            case IType -> {
                control = Control.getITypePreset();

                switch(instruction.opcode) {
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
                control = Control.getJTypePreset();

                if (instruction.opcode == Opcode.j) control.jump = true;
            }
        }
    }

    private void doInstructionDecode() {
        data.registerValue1 = registerTable.get(instruction.rs);
        data.registerValue2 = registerTable.get(instruction.rt);
    }

    private void doExecute() {
        int signExtendedAddress = instruction.immediate;

        int value2 = data.registerValue2;
        if(control.aluSrc) value2 = signExtendedAddress;
        else if(control.aluAddImmediate) value2 = instruction.immediate;

        data.aluOutput = alu.calculate(control.aluOp, data.registerValue1, value2);
        data.aluZero = (data.aluOutput == 0);
    }

    private void doMemory() {
        if(control.memWrite) {
            dataMemory.write(data.aluOutput, data.registerValue2);
        } else if(control.memRead) {
            data.memRead = dataMemory.get(data.aluOutput);
        }
    }

    private void doWriteBack() {
        if(control.regWrite) {
            RegisterName writeRegister = control.regDst ? instruction.rd : instruction.rt;
            int writeValue = control.memToReg ? data.memRead : data.aluOutput;
            registerTable.set(writeRegister, writeValue);
        }
    }

    private void increasePc() {
        programCounter += 4;

        if(data.aluZero && control.branch) {
            int offset = instruction.immediate << 2;
            programCounter += offset;
        }

        if(control.jump) {
            int address = instruction.pseudoAddress << 2;
            programCounter = (programCounter & 0xF0000000) | address;
        }
    }

    private void simulateCycle() {
        doInstructionFetch();
        doInstructionDecode();
        doExecute();
        doMemory();
        doWriteBack();
        increasePc();
    }

    public void simulate() {
        while(instructionMemory.hasNextInstruction(programCounter)) {
            simulateCycle();
        }
    }

    public int getRegisterValue(RegisterName r) { return registerTable.get(r); }

    public int getProgramCounter() { return programCounter; }
    public InstructionMemory getInstructionMemory() { return instructionMemory; }
}