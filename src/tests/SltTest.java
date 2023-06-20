package tests;

import simulator.Instruction;
import simulator.NaiveProcessor;
import simulator.RegisterName;

public class SltTest extends AssemblyTest {
    public SltTest(NaiveProcessor processor) {
        super(processor);
    }

    @Override
    public void record() {
        Instruction.encodeAddi(RegisterName.t0, RegisterName.zero, (short) -1924, processor.getInstructionMemory());
        Instruction.encodeAddi(RegisterName.t1, RegisterName.zero, (short) 1924, processor.getInstructionMemory());
        Instruction.encodeSlt(RegisterName.t2, RegisterName.t0, RegisterName.t1, processor.getInstructionMemory());
        Instruction.encodeSlt(RegisterName.t3, RegisterName.t1, RegisterName.t0, processor.getInstructionMemory());
    }

    @Override
    public void test() {
        assertEquals(RegisterName.t2, 1);
        assertEquals(RegisterName.t3, 0);
    }
}
