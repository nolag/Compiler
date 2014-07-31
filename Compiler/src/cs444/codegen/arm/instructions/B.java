package cs444.codegen.arm.instructions;

import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.instructions.bases.Branch;

public class B extends Branch {
    public B(final ImmediateStr where) {
        super("b", Condition.AL, where);
    }

    public B(final Condition c, final ImmediateStr where) {
        super("b", c, where);
    }
}
