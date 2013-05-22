package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Shl extends BinInstruction{
    public Shl(final Register reg, final InstructionArg arg, final X86SizeHelper sizeHelper){
        super("shl", reg, arg, sizeHelper, 3);
    }

    public Shl(final Memory reg, final InstructionArg arg, final X86SizeHelper sizeHelper){
        super("shl", reg, arg, sizeHelper, 4);
    }
}
