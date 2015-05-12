package cs444.codegen.arm.instructions;

import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.instructions.InstructionArg;

public class Sbfx extends ArmInstruction {
    public final Condition cond;
    public final Register dest;
    public final Register src;
    public final Immediate8 lsb;
    public final Immediate8 width;

    public Sbfx(final Condition cond, final Register dest, final Register src, final Immediate8 lsb, final Immediate8 width) {
        super(1);
        this.cond = cond;
        this.dest = dest;
        this.src = src;
        this.lsb = lsb;
        this.width = width;
    }

    public Sbfx(final Register dest, final Register src, final Immediate8 lsb, final Immediate8 width) {
        this(Condition.AL, dest, src, lsb, width);
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || src.uses(what);
    }

    @Override
    public String generate() {
        return "sbfx" + cond + " " + src + ", " + dest + ", " + lsb + ", " + width;
    }
}
