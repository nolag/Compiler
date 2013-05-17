package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public class Setle extends SetInstruciton{
    public Setle(final InstructionArg arg, final X86SizeHelper sizeHelper){
        super("setle", arg, sizeHelper);
    }
}
