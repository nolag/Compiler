package cs444.codegen.arm.instructions;

import cs444.codegen.arm.instructions.bases.Branch;

public class B extends Branch {
    public B(String where) {
        super("b", Condition.AL, where);
    }

    public B(Condition c, String where) {
        super("b", c, where);
    }
}
