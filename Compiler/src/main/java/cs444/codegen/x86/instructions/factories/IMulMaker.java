package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.IMul;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class IMulMaker implements UniOpMaker {
    public static final IMulMaker maker = new IMulMaker();

    private IMulMaker() { }

    @Override
    public IMul make(Register arg, SizeHelper<X86Instruction, Size> sizeHelper, Size size) {
        return new IMul(arg, size, sizeHelper);
    }
}
