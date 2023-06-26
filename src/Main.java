import simulator.NaiveProcessor;
import simulator.PipelinedProcessor;
import simulator.Processor;
import tests.*;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Processor processor = new PipelinedProcessor();

        // Run all assembly tests.

        AssemblyTest[] tests = {
                new AddSubTest(processor),
                new ArithmeticTest(processor),
                new LwSwTest(processor),
                new SltTest(processor),
                new JTest(processor),
                new BranchTest(processor)
        };

        System.out.println("Running all tests...");

        for(AssemblyTest test: tests) {
            String testName = test.getClass().getName();

            try {
                processor.reset();
                test.record();
                processor.simulate();
                test.test();

                System.out.println("Test '" + testName + "' succeeded.");
            } catch(Exception e) {
                System.err.println("Test '" + testName + "' failed: '" + e.getMessage() + "'");

                for(StackTraceElement element: e.getStackTrace()) {
                    System.err.println(element);
                }
            }
        }
    }
}