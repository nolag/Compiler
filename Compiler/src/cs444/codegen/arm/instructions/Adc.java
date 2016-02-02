package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.DataProcessingWithDestination;

public class Adc extends DataProcessingWithDestination {
    public Adc(final boolean s, final Condition when, final Register dest, final Register rhs, final Operand2 lhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("adc", s, when, dest, rhs, lhs, sizeHelper);
    }

    public Adc(final Register dest, final Register rhs, final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("adc", dest, rhs, lhs, sizeHelper);
    }

    public Adc(final boolean s, final Register dest, final Register rhs, final Operand2 lhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("adc", s, dest, rhs, lhs, sizeHelper);
    }

    public Adc(final Condition when, final Register dest, final Register rhs, final Operand2 lhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("adc", when, dest, rhs, lhs, sizeHelper);
    }
}
