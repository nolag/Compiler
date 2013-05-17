package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Setle;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public class SetleMaker implements UniOpMaker {
    public static SetleMaker maker = new SetleMaker();

    private SetleMaker(){ }

    @Override
    public Setle make(final InstructionArg arg, final X86SizeHelper sizeHelper){
        return new Setle(arg, sizeHelper);
    }
}
