package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class AddOpMaker implements BinOpRegMaker {
    public static AddOpMaker maker = new AddOpMaker();

    private AddOpMaker() {}

    @Override
    public Add make(boolean s, Register dest, Register lhs, Register rhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Add(s, dest, lhs, rhs, sizeHelper);
    }
}
