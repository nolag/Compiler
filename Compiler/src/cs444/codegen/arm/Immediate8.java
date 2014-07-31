package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class Immediate8 extends InstructionArg<ArmInstruction, Size> implements Operand2 {
    public final char value;

    public Immediate8(final char value) {
        this.value = value;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String getValue(Size size, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return Character.toString(value);
    }

    @Override
    public ShiftType getShiftType() {
        return ShiftType.NO_SHIFT;
    }
}
