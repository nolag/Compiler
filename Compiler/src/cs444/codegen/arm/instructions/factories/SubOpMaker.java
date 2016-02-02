package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Sub;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class SubOpMaker implements BinOpRegMaker {
    public static SubOpMaker maker = new SubOpMaker();

    private SubOpMaker() {}

    @Override
    public Sub make(boolean s, Register dest, Register lhs, Register rhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Sub(s, dest, lhs, rhs, sizeHelper);
    }
}
