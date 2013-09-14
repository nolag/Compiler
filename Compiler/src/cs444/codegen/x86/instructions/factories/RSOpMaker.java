package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Sar;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class RSOpMaker implements BinOpMaker {
    public static final RSOpMaker maker = new RSOpMaker();

    private RSOpMaker () { }

    @Override
    public Sar make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sar(one, two, sizeHelper);
    }

    @Override
    public X86Instruction make(final Register one, final Register two, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Sar(one, two, sizeHelper);
    }
}
