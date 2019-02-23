package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Mul;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;

public class MulOpMaker implements BinOpRegMaker {
    public static MulOpMaker maker = new MulOpMaker();

    private MulOpMaker() {}

    @Override
    public Mul make(boolean s, Register dest, Register lhs, Register rhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Mul(s, Condition.AL, dest, lhs, rhs, sizeHelper);
    }
}
