package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Setge;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class SetgeMaker implements UniOpMaker {
    public static SetgeMaker maker = new SetgeMaker();

    private SetgeMaker() { }

    @Override
    public Setge make(Register arg, SizeHelper<X86Instruction, Size> sizeHelper, Size size) {
        return new Setge(arg, sizeHelper);
    }
}
