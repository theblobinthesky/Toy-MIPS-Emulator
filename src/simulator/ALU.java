package simulator;

public class ALU {
    public enum ALUOperation {
        add, sub, and, or, slt
    }

    public int calculate(ALUOperation op, int s, int t) {
        return switch(op) {
            case add -> s + t;
            case sub -> s - t;
            case and -> s & t;
            case or -> s | t;
            case slt -> (s < t) ? 1 : 0;
        };
    }
}
