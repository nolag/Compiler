package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Orr;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class OrrOpMaker implements BinOpRegMaker {
    public static OrrOpMaker maker = new OrrOpMaker();

    private OrrOpMaker() {}

    @Override
    public Orr make(boolean s, Register dest, Register lhs, Register rhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Orr(s, dest, lhs, rhs, sizeHelper);
    }
}
