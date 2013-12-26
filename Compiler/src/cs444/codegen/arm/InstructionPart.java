package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.InstructionArg.Size;
import cs444.codegen.arm.instructions.bases.ARMInstruction;


public interface InstructionPart {
    String getValue(final SizeHelper<ARMInstruction, Size> sizeHelper);
    boolean uses(InstructionArg what);
}
