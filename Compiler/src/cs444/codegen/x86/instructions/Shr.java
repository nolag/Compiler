package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Shr extends BinInstruction {
    public Shr(final Register reg, final Immediate arg, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("shr", reg, arg, sizeHelper, sizeHelper.getDefaultSize(), Size.LOW, 2);
    }

    public Shr(final Register reg, final Immediate arg, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("shr", reg, arg, sizeHelper, size, Size.LOW, 2);
    }

    public Shr(final Register reg, final InstructionArg arg, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("shr", reg, arg, sizeHelper, sizeHelper.getDefaultSize(), Size.LOW, 3);
    }

    public Shr(final Register reg, final InstructionArg arg, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("shr", reg, arg, sizeHelper, size, Size.LOW, 3);
    }

    public Shr(final Memory reg, final InstructionArg arg, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("shr", reg, arg, sizeHelper, sizeHelper.getDefaultSize(), Size.LOW, 4);
    }

    public Shr(final Memory reg, final InstructionArg arg, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("shr", reg, arg, sizeHelper, size, Size.LOW, 4);
    }
}
