package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class AddMemoryFormat extends MemoryFormat {
    public final InstructionPart lhs;
    public final  NotMemory rhs;

    public AddMemoryFormat(final NotMemory lhs, final NotMemory rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public AddMemoryFormat(final MemoryFormat  lhs, final NotMemory rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String getValue(final SizeHelper<X86Instruction, Size> sizeHelper) {
        return lhs.getValue(sizeHelper) + " + " + rhs.getValue(sizeHelper);
    }

    @Override
    public boolean uses(final InstructionArg what) {
        return what == lhs || rhs.uses(what);
    }
}