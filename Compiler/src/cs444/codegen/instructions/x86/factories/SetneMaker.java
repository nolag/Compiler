package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Setne;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public class SetneMaker implements UniOpMaker {
    public static SetneMaker maker = new SetneMaker();

    private SetneMaker(){ }

    @Override
    public Setne make(final InstructionArg arg, final X86SizeHelper sizeHelper) {
        return new Setne(arg, sizeHelper);
    }
}
