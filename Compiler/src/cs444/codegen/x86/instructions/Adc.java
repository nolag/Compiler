package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Adc extends BinInstruction {
    public Adc(final Register minuend, final Register subtrahend, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("adc", minuend, subtrahend, sizeHelper, 1, 2);
    }

    public Adc(final Register minuend, final Register subtrahend, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("adc", minuend, subtrahend, sizeHelper, size, 1, 2);
    }

    public Adc(final Register minuend, final Immediate subtrahend, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("adc", minuend, subtrahend, sizeHelper, 1, 4);
    }

    public Adc(final Register minuend, final Immediate subtrahend, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("adc", minuend, subtrahend, sizeHelper, size, 1, 4);
    }

    public Adc(final Register minuend, final Memory subtrahend, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("adc", minuend, subtrahend, sizeHelper, 2, 4);
    }

    public Adc(final Register minuend, final Memory subtrahend, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("adc", minuend, subtrahend, sizeHelper, size, 2, 4);
    }

    public Adc(final Memory minuend, final Register subtrahend, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("adc", minuend, subtrahend, sizeHelper, 3, 3);
    }

    public Adc(final Memory minuend, final Register subtrahend, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("adc", minuend, subtrahend, sizeHelper, size, 3, 3);
    }

    public Adc(final Memory minuend, final Immediate subtrahend, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("adc", minuend, subtrahend, sizeHelper, 3, 6);
    }

    public Adc(final Memory minuend, final Immediate subtrahend, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        super("adc", minuend, subtrahend, sizeHelper, size, 3, 6);
    }
}
