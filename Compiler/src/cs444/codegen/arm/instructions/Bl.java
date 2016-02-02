package cs444.codegen.arm.instructions;

import cs444.codegen.arm.instructions.bases.Branch;

public class Bl extends Branch {
    public Bl(final String where) {
        super("bl", Condition.AL, where);
    }

    public Bl(final Condition c, final String where) {
        super("bl", c, where);
    }
}
