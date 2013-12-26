package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ARMInstruction;

public abstract class InstructionArg  implements InstructionPart{
    //TODO figure out what to do with this
    public enum Size { DWORD };
    
    public abstract String getValue(final Size size, final SizeHelper<ARMInstruction, Size> sizeHelper);

    @Override
    public final String getValue(final SizeHelper<ARMInstruction, Size> sizeHelper) {
        return getValue(sizeHelper.getDefaultSize(), sizeHelper);
    }
}
