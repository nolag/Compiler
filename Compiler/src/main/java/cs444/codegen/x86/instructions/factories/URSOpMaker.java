package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Shr;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class URSOpMaker implements BinOpMaker {
    public static final URSOpMaker maker = new URSOpMaker();

    private URSOpMaker () { }

    @Override
    public Shr make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shr(one, two, sizeHelper);
    }

    @Override
    public Shr make(final Register one, final Register two, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shr(one, two, size, sizeHelper);
    }

    @Override
    public Shr make(Register one, Immediate two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shr(one, two, sizeHelper);
    }

    @Override
    public Shr make(Register one, Immediate two, Size size, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shr(one, two, size, sizeHelper);
    }
}
