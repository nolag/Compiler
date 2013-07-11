package cs444.codegen.instructions.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;

public class Shl extends BinInstruction{
    public Shl(final Register reg, final InstructionArg arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shl", reg, arg, sizeHelper, sizeHelper.getDefaultSize(), Size.LOW, 3);
    }

    public Shl(final Memory reg, final InstructionArg arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shl", reg, arg, sizeHelper, sizeHelper.getDefaultSize(), Size.LOW, 4);
    }
}
