package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Sar extends BinInstruction{
    public Sar(final Register reg, final InstructionArg arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("sar", reg, arg, sizeHelper, sizeHelper.getDefaultSize(), Size.LOW, 3, 3);
    }

    public Sar(final Register reg, final InstructionArg arg, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("sar", reg, arg, sizeHelper, size, Size.LOW, 3, 3);
    }

    public Sar(final Memory reg, final InstructionArg arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("sar", reg, arg, sizeHelper, sizeHelper.getDefaultSize(), Size.LOW, 3, 5);
    }

    public Sar(final Memory reg, final InstructionArg arg, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("sar", reg, arg, sizeHelper, size, Size.LOW, 3, 5);
    }
}
