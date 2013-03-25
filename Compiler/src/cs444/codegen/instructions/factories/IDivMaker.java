package cs444.codegen.instructions.factories;

import cs444.codegen.InstructionArg;
import cs444.codegen.instructions.IDiv;

public class IDivMaker implements BinUniOpMaker{
    public static final IDivMaker maker = new IDivMaker();

    private IDivMaker() { }

    @Override
    public IDiv make(InstructionArg arg) {
        return new IDiv(arg);
    }
}
