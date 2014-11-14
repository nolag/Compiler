package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.instructions.InstructionArg;

public class Muls extends ArmInstruction {
    public final Condition c;
    public final Register dest;
    public final Register lhs;
    public final Register rhs;
    public final Register sub;
    public final SizeHelper<ArmInstruction, Size> sizeHelper;

    public Muls(final Condition c, final Register dest, final Register lhs, final Register rhs, final Register sub,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super(2);
        this.c = c;
        this.dest = dest;
        this.lhs = lhs;
        this.rhs = rhs;
        this.sub = sub;
        this.sizeHelper = sizeHelper;
    }

    public Muls(final Register dest, final Register lhs, final Register rhs, final Register sub,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(Condition.AL, dest, lhs, rhs, sub, sizeHelper);
    }

    @Override
    public final boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || lhs.uses(what) || rhs.equals(what) || sub.uses(what);
    }

    @Override
    public final String generate() {
        return "muls" + c + " " + dest.getValue(sizeHelper) + ", " + lhs.getValue(sizeHelper) + ", " + rhs.getValue(sizeHelper) + ", "
                + sub.getValue(sizeHelper);
    }
}
