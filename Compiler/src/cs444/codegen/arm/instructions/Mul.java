package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.MulDiv;

public class Mul extends MulDiv {

    public Mul(final Condition c, final Register dest, final Register lhs, final Register rhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("mul", c, dest, lhs, rhs, sizeHelper);
    }

    public Mul(final Register dest, final Register lhs, final Register rhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("mul", dest, lhs, rhs, sizeHelper);
    }
}
