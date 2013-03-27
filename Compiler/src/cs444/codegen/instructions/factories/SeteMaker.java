package cs444.codegen.instructions.factories;

import cs444.codegen.InstructionArg;
import cs444.codegen.instructions.Sete;

public class SeteMaker implements UniOpMaker {
    public static SeteMaker maker = new SeteMaker();

    private SeteMaker(){ }

    @Override
    public Sete make(InstructionArg arg) {
        return new Sete(arg);
    }
}
