package simulator;

public interface Processor {
    InstructionMemory getInstructionMemory();
    int getRegisterValue(RegisterName register);
    int getProgramCounter();
    void reset();
    void simulate();
}
