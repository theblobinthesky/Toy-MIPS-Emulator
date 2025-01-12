package tests;

import simulator.Instruction;
import simulator.NaiveProcessor;
import simulator.Processor;
import simulator.RegisterName;

public class AddSubTest extends AssemblyTest {
    public AddSubTest(Processor processor) {
        super(processor);
    }

    @Override
    public void record() {
        Instruction.encodeAddi(RegisterName.t2, RegisterName.zero, (short) -1924, processor.getInstructionMemory());
        Instruction.encodeAddi(RegisterName.t3, RegisterName.zero, (short) 128, processor.getInstructionMemory());
        Instruction.encodeAddi(RegisterName.t4, RegisterName.zero, (short) 9127, processor.getInstructionMemory());

        Instruction.encodeAdd(RegisterName.t0, RegisterName.t2, RegisterName.t3, processor.getInstructionMemory());
        Instruction.encodeAdd(RegisterName.t1, RegisterName.t3, RegisterName.t4, processor.getInstructionMemory());
        Instruction.encodeSub(RegisterName.t0, RegisterName.t0, RegisterName.t1, processor.getInstructionMemory());
    }

    @Override
    public void test() {
        assertEquals(RegisterName.t2, -1924);
        assertEquals(RegisterName.t3, 128);
        assertEquals(RegisterName.t4, 9127);
        assertEquals(RegisterName.t1, 128 + 9127);
        assertEquals(RegisterName.t0, -11051);
    }
}
