package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Cmp extends BinInstruction{
    public Cmp(final Register arg1, final Memory arg2, final X86SizeHelper sizeHelper){
        super("cmp", arg1, arg2, sizeHelper, 2);
    }

    public Cmp(final Register arg1, final Memory arg2, final X86SizeHelper sizeHelper, final Size size){
        super("cmp", arg1, arg2, sizeHelper, size, 2);
    }

    public Cmp(final Register arg1, final NotMemory arg2, final X86SizeHelper sizeHelper){
        super("cmp", arg1, arg2, sizeHelper, 1);
    }

    public Cmp(final Register arg1, final NotMemory arg2, final X86SizeHelper sizeHelper, final Size size){
        super("cmp", arg1, arg2, sizeHelper, size, 1);
    }


    public Cmp(final Memory arg1, final NotMemory arg2, final X86SizeHelper sizeHelper){
        super("cmp", arg1, arg2, sizeHelper, 2);
    }

    public Cmp(final Memory arg1, final NotMemory arg2, final X86SizeHelper sizeHelper, final Size size){
        super("cmp", arg1, arg2, sizeHelper, size, 2);
    }
}
