package tests;

import simulator.Instruction;
import simulator.NaiveProcessor;
import simulator.Processor;
import simulator.RegisterName;

public class BranchTest extends AssemblyTest {
    public BranchTest(Processor processor) {
        super(processor);
    }

    @Override
    public void record() {
        Instruction.encodeAddi(RegisterName.t0, RegisterName.zero, (short) 0, processor.getInstructionMemory());  // 0
        Instruction.encodeAddi(RegisterName.t1, RegisterName.zero, (short) 1, processor.getInstructionMemory());  // 4
        Instruction.encodeBeq(RegisterName.t0, RegisterName.t1, (short) 16, processor.getInstructionMemory());    // 8
        Instruction.encodeAddi(RegisterName.t2, RegisterName.zero, (short) 42, processor.getInstructionMemory()); // 12

        Instruction.encodeAddi(RegisterName.t0, RegisterName.zero, (short) 2, processor.getInstructionMemory());  // 16
        Instruction.encodeAddi(RegisterName.t1, RegisterName.zero, (short) 2, processor.getInstructionMemory());  // 20
        Instruction.encodeBeq(RegisterName.t0, RegisterName.t1, (short) 32, processor.getInstructionMemory());    // 24
        Instruction.encodeAddi(RegisterName.t3, RegisterName.zero, (short) 42, processor.getInstructionMemory()); // 28
    }

    @Override
    public void test() {
        assertEquals(RegisterName.t2, 42);
        assertEquals(RegisterName.t3, 0);
    }
}
