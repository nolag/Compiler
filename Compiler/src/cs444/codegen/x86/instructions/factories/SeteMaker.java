package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Sete;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;


public class SeteMaker implements UniOpMaker {
    public static SeteMaker maker = new SeteMaker();

    private SeteMaker(){ }

    @Override
    public Sete make(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper){
        return new Sete(arg, sizeHelper);
    }
}