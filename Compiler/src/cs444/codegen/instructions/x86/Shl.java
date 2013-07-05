package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;

public class Shl extends BinInstruction{
    public Shl(final Register reg, final InstructionArg arg, final X86SizeHelper sizeHelper){
        super("shl", reg, arg, sizeHelper, sizeHelper.defaultStack, Size.LOW, 3);
    }

    public Shl(final Memory reg, final InstructionArg arg, final X86SizeHelper sizeHelper){
        super("shl", reg, arg, sizeHelper, sizeHelper.defaultStack, Size.LOW, 4);
    }
}
