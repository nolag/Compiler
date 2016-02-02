package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Extend;

public class Sxth extends Extend {
    public Sxth(Register dst, Register src, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("sxth", dst, src, sizeHelper);
    }
}
