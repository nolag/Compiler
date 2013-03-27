package cs444.codegen.instructions.factories;

import cs444.codegen.InstructionArg;
import cs444.codegen.instructions.Setl;

public class SetlMaker implements UniOpMaker {
    public static SetlMaker maker = new SetlMaker();

    private SetlMaker(){ }

    @Override
    public Setl make(InstructionArg arg) {
        return new Setl(arg);
    }

}
