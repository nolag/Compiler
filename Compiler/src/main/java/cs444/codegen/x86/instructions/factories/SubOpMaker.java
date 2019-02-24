package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Sub;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class SubOpMaker implements BinOpMaker {
    public static final SubOpMaker maker = new SubOpMaker();

    private SubOpMaker() { }

    @Override
    public Sub make(Register one, Register two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sub(one, two, sizeHelper);
    }

    @Override
    public Sub make(Register one, Register two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sub(one, two, size, sizeHelper);
    }

    @Override
    public Sub make(Register one, Immediate two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sub(one, two, sizeHelper);
    }

    @Override
    public Sub make(Register one, Immediate two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sub(one, two, size, sizeHelper);
    }
}
