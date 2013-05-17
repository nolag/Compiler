package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public class Xor extends BinInstruction {
    public Xor(final InstructionArg arg1, final InstructionArg arg2, final X86SizeHelper sizeHelper){
        super("xor", arg1, arg2, sizeHelper);
    }
}
