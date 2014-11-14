package cs444.codegen.arm.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.instructions.InstructionArg;

public abstract class MulDiv extends ArmInstruction {
    private final String name;

    public final Condition c;
    public final Register dest;
    public final Register lhs;
    public final Register rhs;
    public final SizeHelper<ArmInstruction, Size> sizeHelper;

    protected MulDiv(final String name, final Condition c, final Register dest, final Register lhs, final Register rhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        // I don't know how long div takes, based on mul...
        super(2);
        this.name = name;
        this.c = c;
        this.dest = dest;
        this.lhs = lhs;
        this.rhs = rhs;
        this.sizeHelper = sizeHelper;
    }

    public MulDiv(final String name, final Register dest, final Register lhs, final Register rhs,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, Condition.AL, dest, lhs, rhs, sizeHelper);
    }

    @Override
    public final boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || lhs.uses(what) || rhs.equals(what);
    }

    @Override
    public final String generate() {
        return name + c + " " + dest.getValue(sizeHelper) + ", " + lhs.getValue(sizeHelper) + ", " + rhs.getValue(sizeHelper);
    }
}
