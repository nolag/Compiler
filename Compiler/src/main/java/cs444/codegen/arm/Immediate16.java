package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

//0-65535
public class Immediate16 extends InstructionArg<ArmInstruction, Size> {
    public final int value;

    public Immediate16(final int i) {
        this.value = i;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String getValue(Size size, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return toString();
    }

    @Override
    public String toString() {
        return "#" + Integer.toString(value);
    }
}
