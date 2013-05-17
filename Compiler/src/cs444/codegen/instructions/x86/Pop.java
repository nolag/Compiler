package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.X86SizeHelper;

public class Pop implements X86Instruction {
    public final InstructionArg what;
    public final Size size;
    public final X86SizeHelper sizeHelper;

    public Pop(final InstructionArg what, final Size size, final X86SizeHelper sizeHelper){
        this.what = what;
        this.size = size;
        this.sizeHelper = sizeHelper;
    }

    public Pop(final InstructionArg what, final X86SizeHelper sizeHelper){
        this(what, sizeHelper.defaultStack, sizeHelper);
    }

    @Override
    public String generate() {
        return "pop " + InstructionArg.getSizeStr(size) + " " + what.getValue(size, sizeHelper);
    }
}
