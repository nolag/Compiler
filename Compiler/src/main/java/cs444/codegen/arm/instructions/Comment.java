package cs444.codegen.arm.instructions;

import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class Comment extends ArmInstruction {
    public final String comment;

    public Comment(String comment) {
        super(0, 0);
        this.comment = "/* " + comment + " */";
    }

    @Override
    public String generate() {
        return comment;
    }

    @Override
    public final boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }
}
