package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Sub extends BinInstruction {
    public Sub(final Register minuend, final NotMemory subtrahend, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, 1);
    }

    public Sub(final Register minuend, final NotMemory subtrahend, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, size, 1);
    }

    public Sub(final Register minuend, final Memory subtrahend, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, 2);
    }

    public Sub(final Register minuend, final Memory subtrahend, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, size, 2);
    }

    public Sub(final Memory minuend, final NotMemory subtrahend, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, 3);
    }

    public Sub(final Memory minuend, final NotMemory subtrahend, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("sub", minuend, subtrahend, sizeHelper, size, 3);
    }
}
