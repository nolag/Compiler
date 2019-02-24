package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Jb;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class JbMaker implements JxxMaker {

    public static final JbMaker maker = new JbMaker();

    private JbMaker() { }

    @Override
    public Jb make(Immediate where, SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Jb(where, sizeHelper);
    }
}
