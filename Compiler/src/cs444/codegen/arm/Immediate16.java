package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

//0-65535
public class Immediate16 extends InstructionArg<ArmInstruction, Size> {
    public final short value;

    public static final Immediate16 ZERO = new Immediate16((short) 0);
    public static final Immediate16 ONE = new Immediate16((short) 1);

    // same of zero
    public static final Immediate16 NULL = ZERO;
    public static final Immediate16 FALSE = ZERO;

    // same as one
    public static final Immediate16 TRUE = ONE;

    public Immediate16(final short value) {
        this.value = value;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String getValue(Size size, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return Short.toString(value);
    }
}
