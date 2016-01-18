package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Jae;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class JaeMaker implements JxxMaker {

    public static final JaeMaker maker = new JaeMaker();

    private JaeMaker() {}

    @Override
    public Jae make(final Immediate where, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Jae(where, sizeHelper);
    }
}
