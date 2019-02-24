package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class MultiplyMemoryFormat extends MemoryFormat {
    public final Register lhs;
    public final int rhs;

    public MultiplyMemoryFormat(Register lhs, int rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String getValue(SizeHelper<X86Instruction, Size> sizeHelper) {
        return lhs.getValue(sizeHelper) + " * " + rhs;
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        return what == lhs;
    }
}
