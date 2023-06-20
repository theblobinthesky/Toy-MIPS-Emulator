package tests;

import simulator.Instruction;
import simulator.NaiveProcessor;
import simulator.RegisterName;

public class LwSwTest extends AssemblyTest {
    public LwSwTest(NaiveProcessor processor) {
        super(processor);
    }

    @Override
    public void record() {
        Instruction.encodeAddi(RegisterName.t0, RegisterName.zero, (short) 42, processor.getInstructionMemory());
        Instruction.encodeSw(RegisterName.t0, RegisterName.zero, (short) 0, processor.getInstructionMemory());
        Instruction.encodeLw(RegisterName.t1, RegisterName.zero, (short) 0, processor.getInstructionMemory());
    }

    @Override
    public void test() {
        assertEquals(RegisterName.t1, 42);
    }
}
