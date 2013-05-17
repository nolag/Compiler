package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.X86SizeHelper;

public class Cmp extends BinInstruction{
    public Cmp(final InstructionArg arg1, final InstructionArg arg2, final X86SizeHelper sizeHelper){
        super("cmp", arg1, arg2, sizeHelper);
    }

    public Cmp(final InstructionArg arg1, final InstructionArg arg2, final X86SizeHelper sizeHelper, final Size size){
        super("cmp", arg1, arg2, sizeHelper, size);
    }
}
