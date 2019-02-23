package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Jl;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class JlMaker implements JxxMaker {
    public static final JlMaker maker = new JlMaker();

    private JlMaker() { }

    @Override
    public Jl make(final Immediate where, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Jl(where, sizeHelper);
    }
}
