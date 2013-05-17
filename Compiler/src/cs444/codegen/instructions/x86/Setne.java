package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public class Setne extends SetInstruciton{
    public Setne(final InstructionArg arg, final X86SizeHelper sizeHelper){
        super("setne", arg, sizeHelper);
    }
}
