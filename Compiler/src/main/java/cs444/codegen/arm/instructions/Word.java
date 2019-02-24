package cs444.codegen.arm.instructions;

import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class Word extends ArmInstruction {
    public static final Word zeroWord = new Word("0");
    public final String val;

    public Word(String val) {
        super(0, 0);
        this.val = val;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String generate() {
        return ".word " + val;
    }
}
