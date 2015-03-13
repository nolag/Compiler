package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Adc;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class AdcOpMaker implements BinOpRegMaker {
    public static AdcOpMaker maker = new AdcOpMaker();

    private AdcOpMaker() {}

    @Override
    public Adc make(boolean s, Register dest, Register lhs, Register rhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Adc(s, dest, lhs, rhs, sizeHelper);
    }
}
