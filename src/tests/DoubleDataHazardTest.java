package tests;

import simulator.Instruction;
import simulator.Processor;
import simulator.RegisterName;

public class DoubleDataHazardTest extends AssemblyTest {
    public DoubleDataHazardTest(Processor processor) {
        super(processor);
    }

    @Override
    public void record() {
        Instruction.encodeAddi(RegisterName.t0, RegisterName.zero, (short) 200, processor.getInstructionMemory());

        Instruction.encodeAdd(RegisterName.t1, RegisterName.t1, RegisterName.t0, processor.getInstructionMemory());
        Instruction.encodeAdd(RegisterName.t1, RegisterName.t1, RegisterName.t0, processor.getInstructionMemory());
        Instruction.encodeAdd(RegisterName.t1, RegisterName.t1, RegisterName.t0, processor.getInstructionMemory());
    }

    @Override
    public void test() {
        assertEquals(RegisterName.t1, 200 * 3);
    }
}
