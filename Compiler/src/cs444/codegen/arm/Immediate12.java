package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ARMInstruction;

//0-4095
public class Immediate12 extends InstructionArg {
    public final short value;
    
    public Immediate12(final short value) {
        this.value = value;
    }
    
    @Override
    public boolean uses(InstructionArg what) {
        return false;
    }

    @Override
    public String getValue(Size size, SizeHelper<ARMInstruction, Size> sizeHelper) {
        return Short.toString(value);
    }
}
