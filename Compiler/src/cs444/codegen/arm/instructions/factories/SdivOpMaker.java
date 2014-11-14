package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Sdiv;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class SdivOpMaker implements BinOpRegMaker {
    public static SdivOpMaker maker = new SdivOpMaker();

    private SdivOpMaker() {}

    @Override
    public Sdiv make(Register dest, Register lhs, Register rhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Sdiv(dest, lhs, rhs, sizeHelper);
    }
}
