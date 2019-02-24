package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class Clz extends ArmInstruction {
    public final Register dest;
    public final Register num;
    public final SizeHelper<ArmInstruction, Size> sizeHelper;

    public Clz(Register dest, Register num, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super(1);
        this.dest = dest;
        this.num = num;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || num.uses(what);
    }

    @Override
    public String generate() {
        return "clz " + dest.getValue(sizeHelper) + ", " + num.getValue(sizeHelper);
    }
}
