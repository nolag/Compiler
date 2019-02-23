package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Je;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class JeMaker implements JxxMaker {

    public static final JeMaker maker = new JeMaker();

    private JeMaker() { }

    @Override
    public Je make(final Immediate where, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Je(where, sizeHelper);
    }
}
