package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Sar extends BinInstruction{
    public Sar(final Register reg, final InstructionArg arg, final X86SizeHelper sizeHelper){
        super("sar", reg, arg, sizeHelper);
    }
}
