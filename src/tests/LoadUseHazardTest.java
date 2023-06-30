package tests;

import simulator.Instruction;
import simulator.Processor;
import simulator.RegisterName;

public class LoadUseHazardTest extends AssemblyTest {
    public LoadUseHazardTest(Processor processor) {
        super(processor);
    }

    @Override
    public void record() {
        Instruction.encodeAddi(RegisterName.t0, RegisterName.zero, (short) 16, processor.getInstructionMemory());
        Instruction.encodeSw(RegisterName.t0, RegisterName.zero, (short) 0, processor.getInstructionMemory());

        Instruction.encodeLw(RegisterName.t1, RegisterName.zero, (short) 0, processor.getInstructionMemory());
        Instruction.encodeAddi(RegisterName.t1, RegisterName.t1, (short) 8, processor.getInstructionMemory());
        Instruction.encodeAddi(RegisterName.t1, RegisterName.t1, (short) 8, processor.getInstructionMemory());
    }

    @Override
    public void test() {
        assertEquals(RegisterName.t1, 32);
    }
}
