package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Sub extends BinInstruction {
    public Sub(final Register minuend, final Register subtrahend, final X86SizeHelper sizeHelper){
        super("sub", minuend, subtrahend, sizeHelper);
    }
}
