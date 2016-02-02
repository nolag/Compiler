package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.DataProcessingWithDestination;

public class Eor extends DataProcessingWithDestination {
    public Eor(final boolean s, final Condition when, final Register dest, final Register rhs, final Operand2 lhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("eor", s, when, dest, rhs, lhs, sizeHelper);
    }

    public Eor(final Register dest, final Register rhs, final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("eor", dest, rhs, lhs, sizeHelper);
    }

    public Eor(final boolean s, final Register dest, final Register rhs, final Operand2 lhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("eor", s, dest, rhs, lhs, sizeHelper);
    }

    public Eor(final Condition when, final Register dest, final Register rhs, final Operand2 lhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("eor", when, dest, rhs, lhs, sizeHelper);
    }

}
