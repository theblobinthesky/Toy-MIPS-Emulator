package tests;

import simulator.Instruction;
import simulator.NaiveProcessor;
import simulator.RegisterName;

public class JTest extends AssemblyTest {
    public JTest(NaiveProcessor processor) {
        super(processor);
    }

    @Override
    public void record() {
        Instruction.encodeJ(99 * 4, processor.getInstructionMemory());
    }

    @Override
    public void test() {
        assertEquals(processor.getProgramCounter(), 99 * 4);
    }
}
