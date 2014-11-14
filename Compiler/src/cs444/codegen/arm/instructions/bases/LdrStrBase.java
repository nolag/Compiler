package cs444.codegen.arm.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate12;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.instructions.InstructionPart;

// TODO there are some more complicated versions with shifts, implement if useful
public abstract class LdrStrBase extends ArmInstruction {
    public final Size size;
    public final Condition condition;
    public final Register dest;
    public final Register dest2;
    public final Register from;
    public final InstructionPart<ArmInstruction, Size> offset;

    private final String name;
    private final SizeHelper<ArmInstruction, Size> sizeHelper;

    private LdrStrBase(final String name, final Size size, final Condition condition, final Register dest, final Register from,
            final InstructionPart<ArmInstruction, Size> offset, final SizeHelper<ArmInstruction, Size> sizeHelper, final Register dest2) {
        super(1);
        this.name = name;
        this.size = size;
        this.condition = condition;
        this.dest = dest;
        this.from = from;
        this.offset = offset;
        this.sizeHelper = sizeHelper;
        this.dest2 = dest2;
    }

    protected LdrStrBase(final String name, final Size size, final Condition condition, final Register dest, final Register from,
            final Register offset, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, size, condition, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(final String name, final Size size, final Condition condition, final Register dest, final Register from,
            final Immediate12 offset, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, size, condition, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(final String name, final Condition condition, final Register dest, final Register from, Register offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper, final Register dest2) {
        this(name, Size.D, condition, dest, from, offset, sizeHelper, dest2);
    }

    protected LdrStrBase(final String name, final Condition condition, final Register dest, final Register from, Immediate12 offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper, final Register dest2) {
        this(name, Size.D, condition, dest, from, offset, sizeHelper, dest2);
    }

    protected LdrStrBase(final String name, final Size size, final Register dest, final Register from, final Register offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, size, Condition.AL, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(final String name, final Size size, final Register dest, final Register from, final Immediate12 offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, size, Condition.AL, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(final String name, final Register dest, final Register from, Register offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper, final Register dest2) {
        this(name, Size.D, Condition.AL, dest, from, offset, sizeHelper, dest2);
    }

    protected LdrStrBase(final String name, final Register dest, final Register from, Immediate12 offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper, final Register dest2) {
        this(name, Size.D, Condition.AL, dest, from, offset, sizeHelper, dest2);
    }

    protected LdrStrBase(final String name, final Register dest, final Register from, final Register offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, Condition.AL, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(final String name, final Register dest, final Register from, final Immediate12 offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, Condition.AL, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(final String name, final Register dest, final Register from, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, sizeHelper.getDefaultSize(), Condition.AL, dest, from, null, sizeHelper, null);
    }

    protected LdrStrBase(final String name, final Register dest, final Register from, final SizeHelper<ArmInstruction, Size> sizeHelper,
            final Register dest2) {
        this(name, Size.D, Condition.AL, dest, from, null, sizeHelper, dest2);
    }

    protected LdrStrBase(final String name, final Size size, final Register dest, final Register from,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, size, Condition.AL, dest, from, null, sizeHelper, null);
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || from.uses(what) || offset.uses(what);
    }

    @Override
    public String generate() {
        final StringBuilder sb = new StringBuilder(name).append(size.toString()).append(" ").append(condition.toString()).append(" ")
                .append(dest.getValue(sizeHelper)).append(", ");

        if (dest2 != null) sb.append(dest2.getValue(sizeHelper)).append(", ");

        sb.append("[").append(from.getValue(sizeHelper));

        if (offset != null) sb.append(offset.getValue(sizeHelper));

        return sb.append("]").toString();
    }
}
