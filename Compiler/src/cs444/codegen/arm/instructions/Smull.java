package cs444.codegen.arm.instructions;

import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.instructions.InstructionArg;

public class Smull extends ArmInstruction {
    public final Condition c;
    public final Register destHi;
    public final Register destLo;
    public final Register lhs;
    public final Register rhs;

    public Smull(Condition c, final Register destHi, final Register destLo, final Register lhs, final Register rhs) {
        super(3);
        this.c = c;
        this.destHi = destHi;
        this.destLo = destLo;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Smull(final Register destHi, final Register destLo, final Register lhs, final Register rhs) {
        this(Condition.AL, destHi, destLo, lhs, rhs);
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return destHi.uses(what) || destLo.uses(what) || lhs.uses(what) || rhs.uses(what);
    }

    @Override
    public String generate() {
        return "smull" + c + " " + destHi + ", " + destLo + ", " + lhs + ", " + rhs;
    }
}
