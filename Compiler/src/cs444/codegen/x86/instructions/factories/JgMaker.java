package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Jg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class JgMaker implements JxxMaker {
    public static final JgMaker maker = new JgMaker();

    private JgMaker() { }

    @Override
    public Jg make(final Immediate where, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Jg(where, sizeHelper);
    }
}
