package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

//0-4095
public class Immediate12 extends InstructionArg<ArmInstruction, Size> {
    public final short value;
    
    public Immediate12(final short value) {
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
