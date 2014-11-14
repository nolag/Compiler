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
    public Orr make(Register dest, Register lhs, Register rhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Orr(dest, lhs, rhs, sizeHelper);
    }
}
