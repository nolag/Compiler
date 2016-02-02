package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.DataProcessingWithDestination;

public class Sbc extends DataProcessingWithDestination {
    public Sbc(final boolean s, final Condition when, final Register dest, final Register rhs, final Operand2 lhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("sbc", s, when, dest, rhs, lhs, sizeHelper);
    }

    public Sbc(final Register dest, final Register rhs, final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("sbc", dest, rhs, lhs, sizeHelper);
    }

    public Sbc(final boolean s, final Register dest, final Register rhs, final Operand2 lhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("sbc", s, dest, rhs, lhs, sizeHelper);
    }

    public Sbc(final Condition when, final Register dest, final Register rhs, final Operand2 lhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("sbc", when, dest, rhs, lhs, sizeHelper);
    }
}
