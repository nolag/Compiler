package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Setl;
import cs444.codegen.x86.InstructionArg;

public class SetlMaker implements UniOpMaker {
    public static SetlMaker maker = new SetlMaker();

    private SetlMaker(){ }

    @Override
    public Setl make(InstructionArg arg) {
        return new Setl(arg);
    }

}
