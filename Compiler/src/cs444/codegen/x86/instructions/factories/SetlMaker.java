package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Setl;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class SetlMaker implements UniOpMaker {
    public static SetlMaker maker = new SetlMaker();

    private SetlMaker(){ }

    @Override
    public Setl  make(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper, final Size size) {
        return new Setl(arg, sizeHelper);
    }
}
