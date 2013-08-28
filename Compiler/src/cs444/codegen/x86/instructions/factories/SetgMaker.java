package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Setg;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class SetgMaker implements UniOpMaker {
    public static SetgMaker maker = new SetgMaker();

    private SetgMaker(){ }

    @Override
    public Setg make(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Setg(arg, sizeHelper);
    }
}
