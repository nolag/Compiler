package cs444.codegen.arm.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Conditional;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.instructions.InstructionArg;

public abstract class DataProcessingWithDestination extends ArmInstruction {
    private final String instruction;
    
    public final boolean s;
    public final Conditional when;
    public final Register dest;
    public final Register rhs;
    public final Operand2 lhs;
    public final SizeHelper<ArmInstruction, Size> sizeHelper;

    protected DataProcessingWithDestination(final String instruction, final boolean s, final Conditional when, 
            final Register dest, final Register rhs, final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        
        //TODO when/if RRX is added, super needs to be called with 2 if lhs is is RRX because of a 1 cycle register stall.
        super(1);
        this.instruction = instruction;
        this.s = s;
        this.when = when;
        this.dest = dest;
        this.rhs = rhs;
        this.lhs = lhs;
        this.sizeHelper = sizeHelper;
    }
    
    protected DataProcessingWithDestination(final String instruction, final Register dest, final Register rhs, 
            final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(instruction, false, Conditional.AL, dest, rhs, lhs, sizeHelper);
    }
    
    protected DataProcessingWithDestination(final String instruction, final boolean s, final Register dest, 
            final Register rhs,  final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(instruction, s, Conditional.AL, dest, rhs, lhs, sizeHelper);
    }
    
    protected DataProcessingWithDestination(final String instruction, final Conditional when, final Register dest,
            final Register rhs, final Operand2 lhs, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(instruction, false, when, dest, rhs, lhs, sizeHelper);
    }
    
    @Override
    public String generate() {
        return instruction + (s ? "S" : "") + when.getValue(sizeHelper) + " " + 
                dest.getValue(sizeHelper) + " " + rhs.getValue(sizeHelper) + " " + lhs.getValue(sizeHelper);
    }
    
    @Override
    public final boolean uses(final InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || lhs.uses(what) || rhs.uses(what);
    }
}
