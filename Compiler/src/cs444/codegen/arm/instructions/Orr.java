package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.DataProcessingWithDestination;

public class Orr extends DataProcessingWithDestination {
    public Orr(final boolean s, final Condition when, final Register dest, final Register rhs, 
            final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("orr", s, when, dest, rhs, lhs, sizeHelper);
    }
    
    public Orr(final Register dest, final Register rhs, final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper){
        super("orr", dest, rhs, lhs, sizeHelper);
    }
    
    public Orr(final boolean s, final Register dest, final Register rhs,  
            final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper){
        super("orr", s, dest, rhs, lhs, sizeHelper);
    }
    
    public Orr(final Condition when, final Register dest, final Register rhs, 
            final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("orr", when, dest, rhs, lhs, sizeHelper);
    }
}
