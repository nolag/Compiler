package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Sub;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;


public class SubOpMaker implements BinOpMaker {
    public static final SubOpMaker maker = new SubOpMaker();

    private SubOpMaker() { }

    @Override
    public Sub make(final Register one, final Register two, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Sub(one, two, sizeHelper);
    }

}
