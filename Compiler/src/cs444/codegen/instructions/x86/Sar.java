package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Sar extends BinInstruction{
    public Sar(final Register reg, final Immediate arg, final X86SizeHelper sizeHelper){
        super("sar", reg, arg, sizeHelper, 3);
    }

    public Sar(final Memory reg, final Immediate arg, final X86SizeHelper sizeHelper){
        super("sar", reg, arg, sizeHelper, 4);
    }
}
