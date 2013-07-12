package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Setle;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;


public class SetleMaker implements UniOpMaker {
    public static SetleMaker maker = new SetleMaker();

    private SetleMaker(){ }

    @Override
    public Setle make(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Setle(arg, sizeHelper);
    }
}
