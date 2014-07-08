package cs444.codegen.arm.instructions.bases;

import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.instructions.InstructionArg;

public abstract class Branch extends ArmInstruction {

    private final String instruction;
    private final ImmediateStr where;

    protected Branch(String instruction, ImmediateStr where) {
        super(0);
        this.instruction = instruction;
        this.where = where;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String generate() {
        return instruction + " " + where;
    }
}
