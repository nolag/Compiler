package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.DataProcessingWithDestination;

public class And extends DataProcessingWithDestination {
    public And(boolean s, Condition when, Register dest, Register rhs,
               Operand2 lhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("and", s, when, dest, rhs, lhs, sizeHelper);
    }

    public And(Register dest, Register rhs, Operand2 lhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("and", dest, rhs, lhs, sizeHelper);
    }

    public And(boolean s, Register dest, Register rhs,
               Operand2 lhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("and", s, dest, rhs, lhs, sizeHelper);
    }

    public And(Condition when, Register dest, Register rhs,
               Operand2 lhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("and", when, dest, rhs, lhs, sizeHelper);
    }
}
