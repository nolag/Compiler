package cs444.codegen.arm.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.instructions.InstructionArg;

public abstract class DataProcessingWithDestination extends ArmInstruction {
    public final boolean s;
    public final Condition when;
    public final Register dest;
    public final Register rhs;
    public final Operand2 lhs;
    private final String instruction;
    private final SizeHelper<ArmInstruction, Size> sizeHelper;

    protected DataProcessingWithDestination(String instruction, boolean s, Condition when,
                                            Register dest,
                                            Register rhs, Operand2 lhs, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        super(1);
        this.instruction = instruction;
        this.s = s;
        this.when = when;
        this.dest = dest;
        this.rhs = rhs;
        this.lhs = lhs;
        this.sizeHelper = sizeHelper;
    }

    protected DataProcessingWithDestination(String instruction, Register dest, Register rhs,
                                            Operand2 lhs,
                                            SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(instruction, false, Condition.AL, dest, rhs, lhs, sizeHelper);
    }

    protected DataProcessingWithDestination(String instruction, boolean s, Register dest,
                                            Register rhs,
                                            Operand2 lhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(instruction, s, Condition.AL, dest, rhs, lhs, sizeHelper);
    }

    protected DataProcessingWithDestination(String instruction, Condition when, Register dest,
                                            Register rhs,
                                            Operand2 lhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(instruction, false, when, dest, rhs, lhs, sizeHelper);
    }

    @Override
    public String generate() {
        return instruction + when + (s ? "s" : "") + " " + dest.getValue(sizeHelper) + ", " + rhs.getValue(sizeHelper) + ", "
                + lhs.getValue(sizeHelper);
    }

    @Override
    public final boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || lhs.uses(what) || rhs.uses(what);
    }
}
