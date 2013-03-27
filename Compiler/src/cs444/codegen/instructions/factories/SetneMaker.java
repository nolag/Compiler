package cs444.codegen.instructions.factories;

import cs444.codegen.InstructionArg;
import cs444.codegen.instructions.Setne;

public class SetneMaker implements UniOpMaker {
    public static SetneMaker maker = new SetneMaker();

    private SetneMaker(){ }

    @Override
    public Setne make(InstructionArg arg) {
        return new Setne(arg);
    }
}
