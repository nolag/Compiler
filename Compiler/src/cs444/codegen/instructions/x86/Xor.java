package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Xor extends BinInstruction {
    public Xor(final Register arg1, final NotMemory arg2, final X86SizeHelper sizeHelper){
        super("xor", arg1, arg2, sizeHelper, 1);
    }

    public Xor(final Register arg1, final Memory arg2, final X86SizeHelper sizeHelper){
        super("xor", arg1, arg2, sizeHelper, 2);
    }

    public Xor(final Memory arg1, final NotMemory arg2, final X86SizeHelper sizeHelper){
        super("xor", arg1, arg2, sizeHelper, 3);
    }
}
