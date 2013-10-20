package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public abstract class MemoryFormat implements InstructionPart {
    public String getMemoryValue(final SizeHelper<X86Instruction, Size> sizeHelper) {
        return "[" + getValue(sizeHelper) + "]";
    }
}
