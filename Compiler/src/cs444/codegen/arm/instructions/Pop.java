package cs444.codegen.arm.instructions;

import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.bases.RegisterListMovement;

public class Pop extends RegisterListMovement {

    public Pop(Register ... registers) {
        super("pop", registers);
    }
}
