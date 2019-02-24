package cs444.codegen.arm.instructions;

import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class Extern extends ArmInstruction {
    private final ImmediateStr what;

    public Extern(ImmediateStr what) {
        super(0);
        this.what = what;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String generate() {
        //Note that extern may be good if another assembler is uesd
        return "/* extern " + what + " */";
    }
}
