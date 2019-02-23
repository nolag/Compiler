package cs444.codegen.arm.instructions;

import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.bases.RegisterListMovement;

public class Push extends RegisterListMovement {
    public Push(Register... registers) {
        super("push", registers);
    }
}
