package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Setle;
import cs444.codegen.x86.InstructionArg;

public class SetleMaker implements UniOpMaker {
    public static SetleMaker maker = new SetleMaker();

    private SetleMaker(){ }

    @Override
    public Setle make(InstructionArg arg) {
        return new Setle(arg);
    }
}
