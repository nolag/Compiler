package cs444.codegen.arm;

import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionPart;

public interface Operand2 extends InstructionPart<ArmInstruction, Size> {
    public static enum Shift {
        ASR, LSL, LSR, ROR;
        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    };
}
