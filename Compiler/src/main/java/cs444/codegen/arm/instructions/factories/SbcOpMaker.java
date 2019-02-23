package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Sbc;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class SbcOpMaker implements BinOpRegMaker {
    public static SbcOpMaker maker = new SbcOpMaker();

    private SbcOpMaker() {}

    @Override
    public Sbc make(boolean s, Register dest, Register lhs, Register rhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Sbc(s, dest, lhs, rhs, sizeHelper);
    }
}
