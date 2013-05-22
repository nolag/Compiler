package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Sub extends BinInstruction {
    public Sub(final Register minuend, final NotMemory subtrahend, final X86SizeHelper sizeHelper){
        super("sub", minuend, subtrahend, sizeHelper, 1);
    }

    public Sub(final Register minuend, final Memory subtrahend, final X86SizeHelper sizeHelper){
        super("sub", minuend, subtrahend, sizeHelper, 2);
    }

    public Sub(final Memory minuend, final NotMemory subtrahend, final X86SizeHelper sizeHelper){
        super("sub", minuend, subtrahend, sizeHelper, 3);
    }
}
