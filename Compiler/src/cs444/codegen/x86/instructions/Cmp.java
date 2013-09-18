package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Cmp extends BinInstruction {
    public Cmp(final Register arg1, final Memory arg2, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("cmp", arg1, arg2, sizeHelper, 2);
    }

    public Cmp(final Register arg1, final Memory arg2, final SizeHelper<X86Instruction, Size> sizeHelper, final Size size) {
        super("cmp", arg1, arg2, sizeHelper, size, 2);
    }

    public Cmp(final Register arg1, final NotMemory arg2, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("cmp", arg1, arg2, sizeHelper, 1);
    }

    public Cmp(final Register arg1, final NotMemory arg2, final SizeHelper<X86Instruction, Size> sizeHelper, final Size size) {
        super("cmp", arg1, arg2, sizeHelper, size, 1);
    }


    public Cmp(final Memory arg1, final NotMemory arg2, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("cmp", arg1, arg2, sizeHelper, 2);
    }

    public Cmp(final Memory arg1, final NotMemory arg2, final SizeHelper<X86Instruction, Size> sizeHelper, final Size size) {
        super("cmp", arg1, arg2, sizeHelper, size, 2);
    }
}
