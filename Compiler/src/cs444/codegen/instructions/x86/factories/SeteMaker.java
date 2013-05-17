package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Sete;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public class SeteMaker implements UniOpMaker {
    public static SeteMaker maker = new SeteMaker();

    private SeteMaker(){ }

    @Override
    public Sete make(final InstructionArg arg, final X86SizeHelper sizeHelper){
        return new Sete(arg, sizeHelper);
    }
}
