package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.instructions.InstructionArg;

public class Sdiv extends ArmInstruction {
    public final Condition c;
    public final Register dest;
    public final Register num;
    public final Register divisor;
    public final SizeHelper<ArmInstruction, Size> sizeHelper;

    public Sdiv(final Condition c, final Register dest, final Register num, final Register divisor,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        // I don't know how long this takes, based on mul...
        super(2);
        this.c = c;
        this.dest = dest;
        this.num = num;
        this.divisor = divisor;
        this.sizeHelper = sizeHelper;
    }

    public Sdiv(final Register dest, final Register num, final Register denom, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(Condition.AL, dest, num, denom, sizeHelper);
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || num.uses(what) || divisor.equals(what);
    }

    @Override
    public String generate() {
        return "sdiv" + c + " " + dest.getValue(sizeHelper) + ", " + num.getValue(sizeHelper) + ", " + divisor.toString();
    }
}
