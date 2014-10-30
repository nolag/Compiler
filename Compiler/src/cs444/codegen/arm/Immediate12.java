package cs444.codegen.arm;

//0-4095
public class Immediate12 extends Immediate16 {
    public Immediate12(final short value) {
        super(value);
        if (value > 4095) throw new IllegalArgumentException("Can't create Immediate12 with value " + value);
    }
}
