package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Jne;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class JneMaker implements JxxMaker {
    public static final JneMaker maker = new JneMaker();

    private JneMaker() { }

    @Override
    public Jne make(final Immediate where, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Jne(where, sizeHelper);
    }
}
