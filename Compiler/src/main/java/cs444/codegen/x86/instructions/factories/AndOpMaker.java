package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.And;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class AndOpMaker implements BinOpMaker {
    public static final AndOpMaker maker = new AndOpMaker();

    private AndOpMaker() { }

    @Override
    public And make(Register one, Register two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new And(one, two, sizeHelper);
    }

    //Size won't matter, each bit is only with the bit of the same size anyways.
    @Override
    public And make(Register one, Register two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new And(one, two, sizeHelper);
    }

    @Override
    public And make(Register one, Immediate two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new And(one, two, sizeHelper);
    }

    //Size won't matter, each bit is only with the bit of the same size anyways.
    @Override
    public And make(Register one, Immediate two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new And(one, two, sizeHelper);
    }
}
