package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Sete;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class SeteMaker implements UniOpMaker {
    public static SeteMaker maker = new SeteMaker();

    private SeteMaker() { }

    @Override
    public Sete make(Register arg, SizeHelper<X86Instruction, Size> sizeHelper, Size size) {
        return new Sete(arg, sizeHelper);
    }
}
