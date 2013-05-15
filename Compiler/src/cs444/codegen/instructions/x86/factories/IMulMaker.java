package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.IMul;
import cs444.codegen.x86.InstructionArg;

public class IMulMaker implements BinUniOpMaker{
    public static final IMulMaker maker = new IMulMaker();

    private IMulMaker() { }

    @Override
    public IMul make(InstructionArg arg) {
        return new IMul(arg);
    }
}
