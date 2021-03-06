package cs444.codegen.arm;

import cs444.codegen.generic.tiles.helpers.TileHelper;

public class Immediate8 extends Immediate12 implements Operand2 {
    public static final Immediate8 ZERO = new Immediate8((char) 0);
    public static final Immediate8 ONE = new Immediate8((char) 1);
    public static final Immediate8 TWO = new Immediate8((char) 2);
    public static final Immediate8 THREE = new Immediate8((char) 2);
    public static final Immediate8 FOUR = new Immediate8((char) 4);
    public static final Immediate8 THIRTY_ONE = new Immediate8((char) 31);
    public static final Immediate8 THIRTY_TWO = new Immediate8((char) 32);
    public static final Immediate8 THIRTY_THREE = new Immediate8((char) 32);
    public static final Immediate8 SIXTY_FOUR = new Immediate8((char) 64);

    public static final Immediate8 NULL = new Immediate8(TileHelper.NULL);

    // same of zero
    public static final Immediate8 FALSE = ZERO;

    // same as one
    public static final Immediate8 TRUE = ONE;

    public Immediate8(char value) {
        super((short) value);
    }
}
