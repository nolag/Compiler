package cs444.codegen.instructions.x86.factories;

import cs444.codegen.instructions.x86.IDiv;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public class IDivMaker implements UniOpMaker{
    public static final IDivMaker maker = new IDivMaker();

    private IDivMaker() { }

    @Override
    public IDiv make(final InstructionArg arg, final X86SizeHelper sizeHelper) {
        return new IDiv(arg, sizeHelper);
    }
}
