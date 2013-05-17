package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.X86SizeHelper;

public class Jne extends UniInstruction{
    public Jne(final Immediate arg1, final X86SizeHelper sizeHelper){
        super("jne", arg1, sizeHelper);
    }
}
