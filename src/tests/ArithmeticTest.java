package tests;

import simulator.Instruction;
import simulator.NaiveProcessor;
import simulator.RegisterName;

public class ArithmeticTest extends AssemblyTest {
    public ArithmeticTest(NaiveProcessor processor) {
        super(processor);
    }

    @Override
    public void record() {
        Instruction.encodeAddi(RegisterName.t0, RegisterName.zero, (short) 1828, processor.getInstructionMemory());
        Instruction.encodeAddi(RegisterName.t1, RegisterName.zero, (short) -751, processor.getInstructionMemory());
        Instruction.encodeAddi(RegisterName.t2, RegisterName.zero, (short) 2189, processor.getInstructionMemory());

        Instruction.encodeAdd(RegisterName.t3, RegisterName.t0, RegisterName.t1, processor.getInstructionMemory());
        Instruction.encodeAnd(RegisterName.t3, RegisterName.t3, RegisterName.t2, processor.getInstructionMemory());
        Instruction.encodeOr(RegisterName.t3, RegisterName.t3, RegisterName.t0, processor.getInstructionMemory());
    }

    @Override
    public void test() {
        assertEquals(RegisterName.t3, 1829);
    }
}
