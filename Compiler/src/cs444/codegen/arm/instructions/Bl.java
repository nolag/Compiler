package cs444.codegen.arm.instructions;

import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.instructions.bases.Branch;

public class Bl extends Branch {
    public Bl(ImmediateStr where) {
        super("bl", where);
    }
}
