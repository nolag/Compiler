package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Sbb extends BinInstruction {
    public Sbb(Register minuend, Register subtrahend, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sbb", minuend, subtrahend, sizeHelper, 1, 2);
    }

    public Sbb(Register minuend, Register subtrahend, Size size, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        super("sbb", minuend, subtrahend, sizeHelper, size, 1, 2);
    }

    public Sbb(Register minuend, Immediate subtrahend, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sbb", minuend, subtrahend, sizeHelper, 1, 4);
    }

    public Sbb(Register minuend, Immediate subtrahend, Size size, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        super("sbb", minuend, subtrahend, sizeHelper, size, 1, 4);
    }

    public Sbb(Register minuend, Memory subtrahend, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sbb", minuend, subtrahend, sizeHelper, 2, 4);
    }

    public Sbb(Register minuend, Memory subtrahend, Size size, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        super("sbb", minuend, subtrahend, sizeHelper, size, 2, 4);
    }

    public Sbb(Memory minuend, Register subtrahend, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sbb", minuend, subtrahend, sizeHelper, 3, 3);
    }

    public Sbb(Memory minuend, Register subtrahend, Size size, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        super("sbb", minuend, subtrahend, sizeHelper, size, 3, 3);
    }

    public Sbb(Memory minuend, Immediate subtrahend, SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sbb", minuend, subtrahend, sizeHelper, 3, 6);
    }

    public Sbb(Memory minuend, Immediate subtrahend, Size size, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        super("sbb", minuend, subtrahend, sizeHelper, size, 3, 6);
    }
}
