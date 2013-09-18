package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Setge;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class SetgeMaker implements UniOpMaker {
    public static SetgeMaker maker = new SetgeMaker();

    private SetgeMaker(){ }

    @Override
    public Setge make(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper, final Size size) {
        return new Setge(arg, sizeHelper);
    }
}
