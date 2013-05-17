package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.X86SizeHelper;

public class Mov extends BinInstruction{
    public Mov(final InstructionArg dest, final InstructionArg src, final Size size, final X86SizeHelper sizeHelper){
        super("mov", dest, src, sizeHelper);
    }

    public Mov(final InstructionArg dest, final InstructionArg src, final X86SizeHelper sizeHelper){
        super("mov", dest, src, sizeHelper);
    }
}
