package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Sar;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class RSOpMaker implements BinOpMaker {
    public static final RSOpMaker maker = new RSOpMaker();

    private RSOpMaker() { }

    @Override
    public Sar make(Register one, Register two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sar(one, two, sizeHelper);
    }

    @Override
    public Sar make(Register one, Register two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sar(one, two, size, sizeHelper);
    }

    @Override
    public Sar make(Register one, Immediate two, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sar(one, two, sizeHelper);
    }

    @Override
    public Sar make(Register one, Immediate two, Size size,
                    SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sar(one, two, size, sizeHelper);
    }
}
