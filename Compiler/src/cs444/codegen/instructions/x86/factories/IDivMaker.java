package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.IDiv;
import cs444.codegen.x86.InstructionArg;

public class IDivMaker implements BinUniOpMaker{
    public static final IDivMaker maker = new IDivMaker();

    private IDivMaker() { }

    @Override
    public IDiv make(InstructionArg arg) {
        return new IDiv(arg);
    }
}
