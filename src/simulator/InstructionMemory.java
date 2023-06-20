package simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class InstructionMemory {
    ArrayList<Instruction> instructions = new ArrayList<>();

    public void add(Instruction instruction) {
        instructions.add(instruction);
    }

    public Instruction fetchInstruction(int address) {
        assert address % 4 == 0;
        int index = address / 4;

        if(hasNextInstruction(address)) return instructions.get(index);
        else return null;
    }

    public boolean hasNextInstruction(int address) {
        assert address % 4 == 0;
        int index = address / 4;

        return index >= 0 && index < instructions.size();
    }

    public int getLastInstructionAddress() {
        return instructions.size() * 4;
    }
    public void zeroOutEverything() {
        instructions.clear();
    }
}
