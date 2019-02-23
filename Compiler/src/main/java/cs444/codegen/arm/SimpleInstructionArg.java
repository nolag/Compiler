package cs444.codegen.arm;

import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public abstract class SimpleInstructionArg extends InstructionArg<ArmInstruction, Size> {
    @Override
    public final boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return this == what;
    }
}
