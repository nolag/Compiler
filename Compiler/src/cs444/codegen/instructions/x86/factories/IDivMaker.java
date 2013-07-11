package cs444.codegen.instructions.x86.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.IDiv;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;


public class IDivMaker implements UniOpMaker{
    public static final IDivMaker maker = new IDivMaker();

    private IDivMaker() { }

    @Override
    public IDiv make(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new IDiv(arg, sizeHelper);
    }
}
