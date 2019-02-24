package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Or;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class OrOpMaker implements BinOpMaker {
    public static final OrOpMaker maker = new OrOpMaker();

    private OrOpMaker() { }

    @Override
    public Or make(Register one, Register two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Or(one, two, sizeHelper);
    }

    //Size won't matter, each bit is only with the same size anyways
    @Override
    public X86Instruction make(Register one, Register two, Size size,
                               SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Or(one, two, sizeHelper);
    }

    @Override
    public Or make(Register one, Immediate two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Or(one, two, sizeHelper);
    }

    //Size won't matter, each bit is only with the same size anyways
    @Override
    public X86Instruction make(Register one, Immediate two, Size size,
                               SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Or(one, two, sizeHelper);
    }
}
