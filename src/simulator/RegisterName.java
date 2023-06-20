package simulator;

public enum RegisterName {
    zero,
    t0, t1, t2, t3, t4, t5, t6, t7,
    s0, s1, s2, s3, s4, s5, s6, s7,
    t8, t9,
    // You can add more if you like. These don't match the real mips spec.
    sp, fp, ra,
    count
}
