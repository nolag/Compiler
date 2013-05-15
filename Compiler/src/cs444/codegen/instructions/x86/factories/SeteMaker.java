package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Sete;
import cs444.codegen.x86.InstructionArg;

public class SeteMaker implements UniOpMaker {
    public static SeteMaker maker = new SeteMaker();

    private SeteMaker(){ }

    @Override
    public Sete make(InstructionArg arg) {
        return new Sete(arg);
    }
}
