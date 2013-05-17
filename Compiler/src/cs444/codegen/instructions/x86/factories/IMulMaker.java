package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.IMul;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public class IMulMaker implements UniOpMaker{
    public static final IMulMaker maker = new IMulMaker();

    private IMulMaker() { }

    @Override
    public IMul make(final InstructionArg arg, final X86SizeHelper sizeHelper){
        return new IMul(arg, sizeHelper);
    }
}
