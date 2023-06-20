package simulator;

public class PipelineData {
    public static class InstructionFetch extends PipelineData {
        Instruction instruction;
    }

    public static class InstructionDecode extends PipelineData {
        int register1;
        int register2;
        int address;
    }

    public static class Execute extends PipelineData {
        boolean aluZero;
        int aluOutput;
        int writeToRegister;
    }

    public static class Memory extends PipelineData {
        int readFromMemory;
        int writeToRegister;
    }
}
