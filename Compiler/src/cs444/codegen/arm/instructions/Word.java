package cs444.codegen.arm.instructions;

import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class Word extends ArmInstruction {
    public final int value;

    public Word() {
        this(0);
    }

    public Word(int value) {
        super(0, 0);
        this.value = value;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String generate() {
        return ".word " + value;
    }
}
