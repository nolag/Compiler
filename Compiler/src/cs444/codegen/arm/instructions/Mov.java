package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.instructions.InstructionPart;

public class Mov extends ArmInstruction {
    public final Register dest;
    public final InstructionPart<ArmInstruction, Size> src;
    private final SizeHelper<ArmInstruction, Size> sizeHelper;

    private static int calcCost(Operand2 lhs) {
        switch (lhs.getShiftType()) {
        case NO_SHIFT:
        case CONST_SHIFT:
            return 1;
        case REG_SHIFT:
            return 2;
        default:
            throw new IllegalArgumentException("Shift Type unknown");
        }
    }

    public Mov(final Register dest, final Operand2 src, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super(calcCost(src));
        this.dest = dest;
        this.src = src;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || src.uses(what);
    }

    @Override
    public String generate() {
        return "mov " + dest.getValue(sizeHelper) + " " + src.getValue(sizeHelper);
    }
}
