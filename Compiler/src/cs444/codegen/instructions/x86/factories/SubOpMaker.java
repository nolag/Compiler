package cs444.codegen.instructions.x86.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.Sub;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;


public class SubOpMaker implements BinOpMaker {
    public static final SubOpMaker maker = new SubOpMaker();

    private SubOpMaker() { }

    @Override
    public Sub make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Sub(one, two, sizeHelper);
    }

}
