package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class ConstantShift extends InstructionArg<ArmInstruction, Size> implements Operand2 {
    public final Register reg;
    public final byte shift;
    public final Shift type;

    public ConstantShift(Register reg, byte shift, Shift type) {
        this.reg = reg;
        this.shift = shift;
        this.type = type;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String getValue(Size size, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return reg + ", " + type + " #" + shift;
    }
}
