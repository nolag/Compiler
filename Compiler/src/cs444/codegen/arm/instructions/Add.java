package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Conditional;
import cs444.codegen.arm.InstructionArg.Size;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.bases.ARMInstruction;
import cs444.codegen.arm.instructions.bases.DataProcessingWithDestination;

public class Add extends DataProcessingWithDestination {
    public Add(final boolean s, final Conditional when, final Register dest, final Register rhs, 
            final Operand2 lhs, final SizeHelper<ARMInstruction, Size> sizeHelper) {
        super("add", s, when, dest, rhs, lhs, sizeHelper);
    }
    
    public Add(final Register dest, final Register rhs, final Operand2 lhs, final SizeHelper<ARMInstruction, Size> sizeHelper){
        super("add", dest, rhs, lhs, sizeHelper);
    }
    
    public Add(final boolean s, final Register dest, final Register rhs,  
            final Operand2 lhs, final SizeHelper<ARMInstruction, Size> sizeHelper){
        super("add", s, dest, rhs, lhs, sizeHelper);
    }
    
    public Add(final Conditional when, final Register dest, final Register rhs, 
            final Operand2 lhs, final SizeHelper<ARMInstruction, Size> sizeHelper) {
        super("add", when, dest, rhs, lhs, sizeHelper);
    }
}
