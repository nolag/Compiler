package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Ja;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class JaMaker implements JxxMaker {

    public static final JaMaker maker = new JaMaker();

    private JaMaker() { }

    @Override
    public Ja make(final Immediate where, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Ja(where, sizeHelper);
    }
}
