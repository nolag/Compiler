package cs444.codegen.instructions.x86.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.Sar;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;


public class RSOpMaker implements BinOpMaker {
    public static final RSOpMaker maker = new RSOpMaker();

    private RSOpMaker () { }

    @Override
    public Sar make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Sar(one, two, sizeHelper);
    }
}
