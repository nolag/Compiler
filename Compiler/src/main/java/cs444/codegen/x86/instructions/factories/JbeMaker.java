package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Jbe;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class JbeMaker implements JxxMaker {

    public static final JbeMaker maker = new JbeMaker();

    private JbeMaker() { }

    @Override
    public Jbe make(final Immediate where, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Jbe(where, sizeHelper);
    }
}
