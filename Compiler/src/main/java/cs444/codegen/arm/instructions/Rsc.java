package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.DataProcessingWithDestination;

public class Rsc extends DataProcessingWithDestination {
    public Rsc(boolean s, Condition when, Register dest, Register rhs, Operand2 lhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("rsc", s, when, dest, rhs, lhs, sizeHelper);
    }

    public Rsc(Register dest, Register rhs, Operand2 lhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("rsc", dest, rhs, lhs, sizeHelper);
    }

    public Rsc(boolean s, Register dest, Register rhs, Operand2 lhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("rsc", s, dest, rhs, lhs, sizeHelper);
    }

    public Rsc(Condition when, Register dest, Register rhs, Operand2 lhs,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("rsc", when, dest, rhs, lhs, sizeHelper);
    }
}
