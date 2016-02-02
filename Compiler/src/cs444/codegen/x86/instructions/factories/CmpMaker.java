package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Cmp;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class CmpMaker implements BinOpMaker {
    public static CmpMaker maker = new CmpMaker();

    private CmpMaker(){ }

    @Override
    public Cmp make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Cmp(one, two, sizeHelper);
    }

    @Override
    public Cmp make(final Register one, final Register two, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Cmp(one, two, sizeHelper, size);
    }
    
    @Override
    public Cmp make(final Register one, final Immediate two, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Cmp(one, two, sizeHelper);
    }

    @Override
    public Cmp make(final Register one, final Immediate two, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Cmp(one, two, sizeHelper, size);
    }
}
