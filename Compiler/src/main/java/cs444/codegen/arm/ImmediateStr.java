package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class ImmediateStr extends InstructionArg<ArmInstruction, Size> {
    public final String value;

    public ImmediateStr(final String value) {
        this.value = "#" + value;
    }

    @Override
    public boolean uses(final InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String getValue(final Size size, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
