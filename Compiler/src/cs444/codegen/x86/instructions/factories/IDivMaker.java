package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.IDiv;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;


public class IDivMaker implements UniOpMaker{
    public static final IDivMaker maker = new IDivMaker();

    private IDivMaker() { }

    @Override
    public IDiv make(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new IDiv(arg, sizeHelper);
    }
}
