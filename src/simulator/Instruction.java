package simulator;

public class Instruction {
    InstructionType type;
    Opcode opcode;
    Funct funct;
    RegisterName rd = RegisterName.zero;
    RegisterName rs = RegisterName.zero;
    RegisterName rt = RegisterName.zero;
    short immediate;
    int pseudoAddress;

    public enum InstructionType {
        RType, IType, JType
    }
    public static void encodeRType(Opcode opcode, RegisterName rd, RegisterName rs, RegisterName rt, Funct funct, InstructionMemory memory) {
        Instruction instruction = new Instruction();
        instruction.type = InstructionType.RType;
        instruction.opcode = opcode;
        instruction.rd = rd;
        instruction.rs = rs;
        instruction.rt = rt;
        instruction.funct = funct;

        memory.add(instruction);
    }

    public static void encodeIType(Opcode opcode, RegisterName rs, RegisterName rt, short immediate, InstructionMemory memory) {
        Instruction instruction = new Instruction();
        instruction.type = InstructionType.IType;
        instruction.opcode = opcode;
        instruction.rs = rs;
        instruction.rt = rt;
        instruction.immediate = immediate;

        memory.add(instruction);
    }

    public static void encodeJType(Opcode opcode, int pseudoAddress, InstructionMemory memory) {
        Instruction instruction = new Instruction();
        instruction.type = InstructionType.JType;
        instruction.opcode = opcode;
        instruction.pseudoAddress = pseudoAddress;

        memory.add(instruction);
    }

    public static void encodeAdd(RegisterName rd, RegisterName rs, RegisterName rt, InstructionMemory memory) {
        encodeRType(Opcode.arithmetic, rd, rs, rt, Funct.add, memory);
    }

    public static void encodeLw(RegisterName rt, RegisterName rs, short immediate, InstructionMemory memory) {
        encodeIType(Opcode.lw, rs, rt, immediate, memory);
    }

    public static void encodeSw(RegisterName rt, RegisterName rs, short immediate, InstructionMemory memory) {
        encodeIType(Opcode.sw, rs, rt, immediate, memory);
    }

    public static void encodeSub(RegisterName rd, RegisterName rs, RegisterName rt, InstructionMemory memory) {
        encodeRType(Opcode.arithmetic, rd, rs, rt, Funct.sub, memory);
    }

    public static void encodeAnd(RegisterName rd, RegisterName rs, RegisterName rt, InstructionMemory memory) {
        encodeRType(Opcode.arithmetic, rd, rs, rt, Funct.and, memory);
    }

    public static void encodeOr(RegisterName rd, RegisterName rs, RegisterName rt, InstructionMemory memory) {
        encodeRType(Opcode.arithmetic, rd, rs, rt, Funct.or, memory);
    }

    public static void encodeSlt(RegisterName rd, RegisterName rs, RegisterName rt, InstructionMemory memory) {
        encodeRType(Opcode.arithmetic, rd, rs, rt, Funct.slt, memory);
    }

    public static void encodeBeq(RegisterName rs, RegisterName rt, short address, InstructionMemory memory) {
        encodeIType(Opcode.beq, rs, rt, (short)(address >> 2), memory);
    }

    public static void encodeJ(int pseudoAddress, InstructionMemory memory) {
        encodeJType(Opcode.j, (short)(pseudoAddress >> 2), memory);
    }

    public static void encodeAddi(RegisterName rt, RegisterName rs, short immediate, InstructionMemory memory) {
        encodeIType(Opcode.addi, rs, rt, immediate, memory);
    }
}
