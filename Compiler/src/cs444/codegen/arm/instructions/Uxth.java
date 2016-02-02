package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Extend;

public class Uxth extends Extend {
    public Uxth(Register dst, Register src, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("uxth", dst, src, sizeHelper);
    }
}
