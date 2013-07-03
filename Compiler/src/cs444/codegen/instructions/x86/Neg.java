package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Neg extends UniInstruction{
    public Neg(final Memory what, final X86SizeHelper sizeHelper){
        super("neg", what, sizeHelper, 3);
    }

    public Neg(final Register what, final X86SizeHelper sizeHelper){
        super("neg", what, sizeHelper, 1);
    }
}
