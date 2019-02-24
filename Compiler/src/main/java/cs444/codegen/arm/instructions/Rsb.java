package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.DataProcessingWithDestination;

public class Rsb extends DataProcessingWithDestination {
    public Rsb(boolean s, Condition when, Register dest, Register rhs, Operand2 lhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("rsb", s, when, dest, rhs, lhs, sizeHelper);
    }

    public Rsb(Register dest, Register rhs, Operand2 lhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("rsb", dest, rhs, lhs, sizeHelper);
    }

    public Rsb(boolean s, Register dest, Register rhs, Operand2 lhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("rsb", s, dest, rhs, lhs, sizeHelper);
    }

    public Rsb(Condition when, Register dest, Register rhs, Operand2 lhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("rsb", when, dest, rhs, lhs, sizeHelper);
    }
}
