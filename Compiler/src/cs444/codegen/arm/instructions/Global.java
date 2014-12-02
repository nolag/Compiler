package cs444.codegen.arm.instructions;

import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class Global extends ArmInstruction {
    private final String val;

    public Global(final String val) {
        super(0, 0);
        this.val = val;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String generate() {
        return ".global " + val;
    }

}
