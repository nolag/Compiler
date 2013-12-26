package cs444.codegen.arm.instructions.bases;

import cs444.codegen.instructions.Instruction;

public abstract class ARMInstruction extends Instruction {
    protected ARMInstruction(int time) {
        super(time, 4);
    }
    
    protected ARMInstruction(int time, int space) {
        super(time, space);
    }
}
