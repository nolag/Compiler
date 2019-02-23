package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Shr extends BinInstruction {
    public Shr(final Register reg, final InstructionArg<X86Instruction, Size> arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shr", reg, arg, sizeHelper, sizeHelper.getDefaultSize(), Size.LOW, 3, 3);
    }

    public Shr(final Register reg, final InstructionArg<X86Instruction, Size> arg, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shr", reg, arg, sizeHelper, size, Size.LOW, 3, 3);
    }

    public Shr(final Memory reg, final InstructionArg<X86Instruction, Size> arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shr", reg, arg, sizeHelper, sizeHelper.getDefaultSize(), Size.LOW, 3, 5);
    }

    public Shr(final Memory reg, final InstructionArg<X86Instruction, Size> arg, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("shr", reg, arg, sizeHelper, size, Size.LOW, 3, 5);
    }
}
