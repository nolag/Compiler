package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Sub extends BinInstruction {
    public Sub(Register minuend, Register subtrahend, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, 1, 2);
    }

    public Sub(Register minuend, Register subtrahend, Size size, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, size, 1, 2);
    }

    public Sub(Register minuend, Immediate subtrahend, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, 1, 4);
    }

    public Sub(Register minuend, Immediate subtrahend, Size size, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, size, 1, 4);
    }

    public Sub(Register minuend, Memory subtrahend, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, 2, 4);
    }

    public Sub(Register minuend, Memory subtrahend, Size size, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, size, 2, 4);
    }

    public Sub(Memory minuend, Register subtrahend, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, 3, 3);
    }

    public Sub(Memory minuend, Register subtrahend, Size size, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, size, 3, 3);
    }

    public Sub(Memory minuend, Immediate subtrahend, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, 3, 6);
    }

    public Sub(Memory minuend, Immediate subtrahend, Size size, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, size, 3, 6);
    }
}
