package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.DataProcessingWithDestination;

public class Sub extends DataProcessingWithDestination {
    public Sub(final boolean s, final Condition when, final Register dest, final Register rhs, 
            final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("sub", s, when, dest, rhs, lhs, sizeHelper);
    }
    
    public Sub(final Register dest, final Register rhs, final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper){
        super("sub", dest, rhs, lhs, sizeHelper);
    }
    
    public Sub(final boolean s, final Register dest, final Register rhs,  
            final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper){
        super("sub", s, dest, rhs, lhs, sizeHelper);
    }
    
    public Sub(final Condition when, final Register dest, final Register rhs, 
            final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("sub", when, dest, rhs, lhs, sizeHelper);
    }
}
