package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public class IMul extends UniInstruction{
    public IMul(final InstructionArg arg1, final X86SizeHelper sizeHelper){
        super("imul", arg1, sizeHelper);
    }
}
