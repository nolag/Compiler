package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.instructions.InstructionPart;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class AddMemoryFormat extends MemoryFormat {
    public final InstructionPart<X86Instruction, Size> lhs;
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
        if ("0".equals(rhs.getValue(sizeHelper))) return lhs.getValue(sizeHelper);
        if ("0".equals(lhs.getValue(sizeHelper))) return rhs.getValue(sizeHelper);
        return lhs.getValue(sizeHelper) + " + " + rhs.getValue(sizeHelper);
    }

    @Override
    public boolean uses(final InstructionArg<X86Instruction, ?> what) {
        return what == lhs || rhs.uses(what);
    }
}