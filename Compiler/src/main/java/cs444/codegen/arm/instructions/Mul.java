package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.MulDiv;

public class Mul extends MulDiv {

    public Mul(boolean s, Condition c, Register dest, Register lhs, Register rhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super(s, "mul", c, dest, lhs, rhs, sizeHelper);
    }

    public Mul(Condition c, Register dest, Register lhs, Register rhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("mul", c, dest, lhs, rhs, sizeHelper);
    }

    public Mul(Register dest, Register lhs, Register rhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("mul", dest, lhs, rhs, sizeHelper);
    }
}
