package cs444.codegen.arm.instructions;

import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.instructions.InstructionArg;

public class Mal extends ArmInstruction {
    public final Condition c;
    public final Register dest;
    public final Register lhs;
    public final Register rhs;
    public final Register add;

    public Mal(Condition c, Register dest, Register lhs, Register rhs, Register add) {
        super(3);
        this.c = c;
        this.dest = dest;
        this.lhs = lhs;
        this.rhs = rhs;
        this.add = add;
    }

    public Mal(Register dest, Register lhs, Register rhs, Register add) {
        this(Condition.AL, dest, lhs, rhs, add);
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || lhs.uses(what) || rhs.uses(what) || lhs.uses(what) || add.uses(what);
    }

    @Override
    public String generate() {
        return "Mal" + c + " " + dest + ", " + lhs + ", " + rhs + ", " + add;
    }
}
