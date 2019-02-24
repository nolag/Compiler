package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public interface BinOpRegMaker {
    ArmInstruction make(boolean s, Register dest, Register lhs, Register rhs,
                        SizeHelper<ArmInstruction, Size> sizeHelper);
}
