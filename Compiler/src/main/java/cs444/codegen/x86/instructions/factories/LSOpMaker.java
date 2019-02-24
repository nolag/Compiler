package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Shl;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class LSOpMaker implements BinOpMaker {
    public static final LSOpMaker maker = new LSOpMaker();

    private LSOpMaker() { }

    @Override
    public Shl make(Register one, Register two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shl(one, two, sizeHelper);
    }

    @Override
    public Shl make(Register one, Register two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shl(one, two, size, sizeHelper);
    }

    @Override
    public Shl make(Register one, Immediate two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shl(one, two, sizeHelper);
    }

    @Override
    public Shl make(Register one, Immediate two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Shl(one, two, size, sizeHelper);
    }
}
