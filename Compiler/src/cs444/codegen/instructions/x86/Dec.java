package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Dec extends UniInstruction{
    public Dec(final Register register, final X86SizeHelper sizeHelper){
        super("dec", register, sizeHelper);
    }
}
