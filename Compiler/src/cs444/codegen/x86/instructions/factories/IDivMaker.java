package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.IDiv;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class IDivMaker implements UniOpMaker{
    public static final IDivMaker maker = new IDivMaker();

    private IDivMaker() { }

    @Override
    public X86Instruction make(final Register arg, final SizeHelper<X86Instruction, Size> sizeHelper, final Size size) {
        return new IDiv(arg, size, sizeHelper);
    }
}
