package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.Setne;
import cs444.codegen.x86.InstructionArg;

public class SetneMaker implements UniOpMaker {
    public static SetneMaker maker = new SetneMaker();

    private SetneMaker(){ }

    @Override
    public Setne make(InstructionArg arg) {
        return new Setne(arg);
    }
}
