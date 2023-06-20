package simulator;

public class PipelineControl {
    boolean regDst;
    boolean branch;
    boolean memRead;
    boolean memToReg;
    int aluOp;
    boolean memWrite;
    boolean aluSrc;
    boolean regWrite;

    public static class InstructionFetch extends PipelineControl {

    }

    public static class InstructionDecode extends PipelineControl {

    }

    public static class Execute  extends PipelineControl {

    }

    public static class Memory extends PipelineControl {

    }

    public static class WriteBack  extends PipelineControl {

    }
}
