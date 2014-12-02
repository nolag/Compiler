package cs444.codegen.arm.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate16;
import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.instructions.InstructionPart;

public abstract class MovBase extends ArmInstruction {
    public final Condition cond;
    public final Register dest;
    public final InstructionPart<ArmInstruction, Size> src;

    private final SizeHelper<ArmInstruction, Size> sizeHelper;
    private final String what;

    private MovBase(final String what, Condition cond, Register dest, InstructionPart<ArmInstruction, Size> src,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super(1);
        this.cond = cond;
        this.what = what;
        this.dest = dest;
        this.src = src;
        this.sizeHelper = sizeHelper;
    }

    public MovBase(final String what, final Condition cond, final Register dest, final Operand2 src,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(what, cond, dest, (InstructionPart<ArmInstruction, Size>) src, sizeHelper);

    }

    public MovBase(final String what, final Condition cond, final Register dest, final Immediate16 imm,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(what, cond, dest, (InstructionPart<ArmInstruction, Size>) imm, sizeHelper);
    }

    public MovBase(final String what, final Condition cond, final Register dest, final ImmediateStr imm,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(what, cond, dest, (InstructionPart<ArmInstruction, Size>) imm, sizeHelper);
    }

    public MovBase(final String what, final Register dest, final Operand2 src, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(what, Condition.AL, dest, src, sizeHelper);
    }

    public MovBase(final String what, final Register dest, final Immediate16 imm, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(what, Condition.AL, dest, imm, sizeHelper);
    }

    public MovBase(final String what, final Register dest, final ImmediateStr imm, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(what, Condition.AL, dest, imm, sizeHelper);
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || src.uses(what);
    }

    @Override
    public String generate() {
        return what + " " + dest.getValue(sizeHelper) + ", " + src.getValue(sizeHelper);
    }
}
