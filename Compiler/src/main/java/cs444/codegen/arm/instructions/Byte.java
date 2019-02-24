package cs444.codegen.arm.instructions;

import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class Byte extends ArmInstruction {
    public final String val;

    protected Byte(String val) {
        super(0, 0);
        this.val = val;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String generate() {
        return ".byte " + val;
    }
}
