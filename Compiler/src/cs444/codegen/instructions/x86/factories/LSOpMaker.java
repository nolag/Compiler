package cs444.codegen.instructions.x86.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.Shl;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;


public class LSOpMaker implements BinOpMaker {
    public static final LSOpMaker maker = new LSOpMaker();

    private LSOpMaker () { }

    @Override
    public Shl make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Shl(one, two, sizeHelper);
    }
}
