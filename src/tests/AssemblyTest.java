package tests;

import simulator.NaiveProcessor;
import simulator.RegisterName;

public abstract class AssemblyTest {
    protected NaiveProcessor processor;

    public AssemblyTest(NaiveProcessor processor) {
        this.processor = processor;
    }

    void assertEquals(int gotValue, int expectedValue) {
        if(gotValue != expectedValue) {
            throw new RuntimeException("assertEquals failed. Expected " + expectedValue + ", but got " + gotValue + ".");
        }
    }

    void assertEquals(RegisterName r, int expectedValue) {
        int gotValue = processor.getRegisterValue(r);

        if(gotValue != expectedValue) {
            throw new RuntimeException("assertEquals failed. Register " + r + " does was expected to be " + expectedValue + ", but got " + gotValue + ".");
        }
    }

    public abstract void record();
    public abstract void test();
}
