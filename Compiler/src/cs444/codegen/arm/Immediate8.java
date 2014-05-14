package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

//0-255
public class Immediate8 extends InstructionArg<ArmInstruction, Size> {
    public final byte value;
    
    public Immediate8(final byte value) {
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
