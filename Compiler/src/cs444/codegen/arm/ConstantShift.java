package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class ConstantShift extends InstructionArg<ArmInstruction, Size> implements Operand2 {
    public final Register reg;
    public final byte shift;
    public final Shift type;

    public ConstantShift(final Register reg, final byte shift, final Shift type) {
        this.reg = reg;
        this.shift = shift;
        this.type = type;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public ShiftType getShiftType() {
        return ShiftType.CONST_SHIFT;
    }

    @Override
    public String getValue(Size size, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return reg.toString() + ", " + type.toString() + " #" + shift;
    }
}
