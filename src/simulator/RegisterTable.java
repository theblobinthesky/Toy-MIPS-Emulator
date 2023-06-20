package simulator;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;

public class RegisterTable {
    int[] registerValues;

    public RegisterTable() {
        registerValues= new int[RegisterName.count.ordinal()];
        registerValues[RegisterName.zero.ordinal()] = 0; // Just to be explicit about this.
    }

    public int get(RegisterName r) {
        if(r == null) return 0;
        else return registerValues[r.ordinal()];
    }

    public void set(RegisterName r, int value) {
        registerValues[r.ordinal()] = value;
    }

    public void zeroOutEverything() {
        Arrays.fill(registerValues, 0);
    }
}
