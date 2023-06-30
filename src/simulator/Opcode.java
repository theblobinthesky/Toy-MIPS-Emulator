package simulator;

public enum Opcode {
    nop(0x00),
    lw(0x23),
    sw(0x2B),
    arithmetic(0x00),
    beq(0x04),
    j(0x02),
    addi(0x08);

    private final int encoding;
    private Opcode(int encoding) { this.encoding = encoding; }
    public int getEncoding() { return encoding; }
}
