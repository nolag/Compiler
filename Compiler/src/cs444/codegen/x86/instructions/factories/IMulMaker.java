package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.IMul;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;


public class IMulMaker implements UniOpMaker{
    public static final IMulMaker maker = new IMulMaker();

    private IMulMaker() { }

    @Override
    public IMul make(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new IMul(arg, sizeHelper);
    }
}
