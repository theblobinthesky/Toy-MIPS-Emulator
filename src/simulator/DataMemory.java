package simulator;

import java.util.HashMap;
import java.util.Map;

public class DataMemory {
    Map<Integer, Integer> memory;

    public DataMemory() {
        memory = new HashMap<>();
    }

    public void write(int address, int value) {
        memory.put(address, value);
    }

    public int get(int address) {
        Integer value = memory.get(address);
        if(value == null) return 0;
        else return value;
    }

    public void zeroOutEverything() {
        memory.clear();
    }
}
