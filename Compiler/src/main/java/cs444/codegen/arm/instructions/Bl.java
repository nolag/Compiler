package cs444.codegen.arm.instructions;

import cs444.codegen.arm.instructions.bases.Branch;

public class Bl extends Branch {
    public Bl(String where) {
        super("bl", Condition.AL, where);
    }

    public Bl(Condition c, String where) {
        super("bl", c, where);
    }
}
