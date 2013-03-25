package cs444.codegen.instructions.factories;

import cs444.codegen.InstructionArg;
import cs444.codegen.instructions.IMul;

public class IMulMaker implements BinUniOpMaker{
    public static final IMulMaker maker = new IMulMaker();

    private IMulMaker() { }

    @Override
    public IMul make(InstructionArg arg) {
        return new IMul(arg);
    }
}
