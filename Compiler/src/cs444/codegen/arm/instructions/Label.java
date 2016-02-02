package cs444.codegen.arm.instructions;

import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;


public class Label extends ArmInstruction{
    private final String lbl;

    public Label(final String lbl){
        super(0, 0);
        this.lbl = lbl + ":";
    }

    @Override
    public String generate() {
        return lbl;
    }

    @Override
    public boolean uses(final InstructionArg<ArmInstruction, ?> what) {
        return false;
    }
}
