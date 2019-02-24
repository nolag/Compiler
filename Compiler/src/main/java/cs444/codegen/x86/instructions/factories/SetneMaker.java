package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Setne;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class SetneMaker implements UniOpMaker {
    public static SetneMaker maker = new SetneMaker();

    private SetneMaker() { }

    @Override
    public X86Instruction make(Register arg, SizeHelper<X86Instruction, Size> sizeHelper, Size size) {
        return new Setne(arg, sizeHelper);
    }
}
