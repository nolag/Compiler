package cs444.codegen.arm.instructions;

import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.instructions.bases.Branch;

public class Bl extends Branch {
    public Bl(final ImmediateStr where) {
        super("bl", Condition.AL, where);
    }

    public Bl(final Condition c, final ImmediateStr where) {
        super("bl", c, where);
    }
}
