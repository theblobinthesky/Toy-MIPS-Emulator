package simulator;

public enum Funct {
    zero(0x00),
    add(0x20),
    sub(0x22),
    and(0x20),
    or(0x25),
    slt(0x2A);

    private final int encoding;
    private Funct(int encoding) { this.encoding = encoding; }
    public int getEncoding() { return encoding; }
}
