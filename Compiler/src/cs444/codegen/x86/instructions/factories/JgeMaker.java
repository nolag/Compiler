package cs444.codegen.x86.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Jge;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class JgeMaker implements JxxMaker {

    public static final JgeMaker maker = new JgeMaker();

    private JgeMaker() { }

    @Override
    public Jge make(final Immediate where, final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Jge(where, sizeHelper);
    }
}
