package cs444.codegen.instructions.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;

public class Shr extends BinInstruction{
    public Shr(final Register reg, final InstructionArg arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shr", reg, arg, sizeHelper, sizeHelper.getDefaultSize(), Size.LOW, 3);
    }

    public Shr(final Memory reg, final InstructionArg arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shr", reg, arg, sizeHelper, sizeHelper.getDefaultSize(), Size.LOW, 4);
    }
}
