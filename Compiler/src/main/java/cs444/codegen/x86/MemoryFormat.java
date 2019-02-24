package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionPart;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public abstract class MemoryFormat implements InstructionPart<X86Instruction, Size> {
    public String getMemoryValue(SizeHelper<X86Instruction, Size> sizeHelper) {
        return "[" + getValue(sizeHelper) + "]";
    }
}
