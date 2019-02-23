package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate12;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public interface Imm12OrRegMaker {
    ArmInstruction make(Register dest, Register r1, Register r2, SizeHelper<ArmInstruction, Size> sizeHelper);

    ArmInstruction make(Register dest, Register r1, Immediate12 imm, SizeHelper<ArmInstruction, Size> sizeHelper);
}
