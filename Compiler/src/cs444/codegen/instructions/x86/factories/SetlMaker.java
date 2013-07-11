package cs444.codegen.instructions.x86.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.Setl;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;


public class SetlMaker implements UniOpMaker {
    public static SetlMaker maker = new SetlMaker();

    private SetlMaker(){ }

    @Override
    public Setl make(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Setl(arg, sizeHelper);
    }
}
