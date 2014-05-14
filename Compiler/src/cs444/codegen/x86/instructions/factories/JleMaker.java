package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Jle;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class JleMaker implements JxxMaker {

    public static final JleMaker maker = new JleMaker();

    private JleMaker() { }

    @Override
    public Jle make(final Immediate where, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Jle(where, sizeHelper);
    }
}
