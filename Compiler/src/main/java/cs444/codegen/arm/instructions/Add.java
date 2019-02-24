package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.DataProcessingWithDestination;

public class Add extends DataProcessingWithDestination {
    public Add(boolean s, Condition when, Register dest, Register rhs,
               Operand2 lhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("add", s, when, dest, rhs, lhs, sizeHelper);
    }

    public Add(Register dest, Register rhs, Operand2 lhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("add", dest, rhs, lhs, sizeHelper);
    }

    public Add(boolean s, Register dest, Register rhs,
               Operand2 lhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("add", s, dest, rhs, lhs, sizeHelper);
    }

    public Add(Condition when, Register dest, Register rhs,
               Operand2 lhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("add", when, dest, rhs, lhs, sizeHelper);
    }
}
