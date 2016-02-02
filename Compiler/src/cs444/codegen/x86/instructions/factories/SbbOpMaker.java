package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Sbb;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class SbbOpMaker implements BinOpMaker {
    public static final SbbOpMaker maker = new SbbOpMaker();

    private SbbOpMaker() { }

    @Override
    public Sbb make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sbb(one, two, sizeHelper);
    }

    @Override
    public Sbb make(final Register one, final Register two, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sbb(one, two, size, sizeHelper);
    }
    
    @Override
    public Sbb make(final Register one, final Immediate two, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sbb(one, two, sizeHelper);
    }

    @Override
    public Sbb make(final Register one, final Immediate two, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sbb(one, two, size, sizeHelper);
    }
}
