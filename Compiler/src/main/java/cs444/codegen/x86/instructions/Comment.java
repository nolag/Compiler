package cs444.codegen.x86.instructions;

import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Comment extends X86Instruction {
    public final String comment;

    public Comment(String comment) {
        super(0, 0);
        this.comment = ";" + comment;
    }

    @Override
    public String generate() {
        return comment;
    }

    @Override
    public final boolean uses(InstructionArg<X86Instruction, ?> what) {
        return false;
    }
}
