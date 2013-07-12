package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Setne;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;


public class SetneMaker implements UniOpMaker {
    public static SetneMaker maker = new SetneMaker();

    private SetneMaker(){ }

    @Override
    public Setne make(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Setne(arg, sizeHelper);
    }
}
