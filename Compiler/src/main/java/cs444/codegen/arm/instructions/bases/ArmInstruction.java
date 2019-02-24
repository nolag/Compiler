package cs444.codegen.arm.instructions.bases;

import cs444.codegen.instructions.Instruction;

public abstract class ArmInstruction extends Instruction<ArmInstruction> {
    protected ArmInstruction(int time) {
        super(time, 4);
    }

    protected ArmInstruction(int time, int space) {
        super(time, space);
    }
}
