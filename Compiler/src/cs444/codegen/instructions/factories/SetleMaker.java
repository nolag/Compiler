package cs444.codegen.instructions.factories;

import cs444.codegen.InstructionArg;
import cs444.codegen.instructions.Setle;

public class SetleMaker implements UniOpMaker {
    public static SetleMaker maker = new SetleMaker();

    private SetleMaker(){ }

    @Override
    public Setle make(InstructionArg arg) {
        return new Setle(arg);
    }
}
