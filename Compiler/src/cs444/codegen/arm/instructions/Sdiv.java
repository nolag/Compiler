package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.MulDiv;

public class Sdiv extends MulDiv {

    public Sdiv(final Condition c, final Register dest, final Register lhs, final Register rhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("sdiv", c, dest, lhs, rhs, sizeHelper);
    }

    public Sdiv(final Register dest, final Register lhs, final Register rhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("sdiv", dest, lhs, rhs, sizeHelper);
    }
}
