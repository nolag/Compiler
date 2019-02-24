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

    private LdrStrBase(String name, Size size, Condition condition, Register dest,
                       Register from,
                       InstructionPart<ArmInstruction, Size> offset,
                       SizeHelper<ArmInstruction, Size> sizeHelper, Register dest2) {
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

    protected LdrStrBase(String name, Size size, Condition condition, Register dest,
                         Register from,
                         Register offset, SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, size, condition, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(String name, Size size, Condition condition, Register dest,
                         Register from,
                         Immediate12 offset, SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, size, condition, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(String name, Condition condition, Register dest, Register from,
                         Register offset,
                         SizeHelper<ArmInstruction, Size> sizeHelper, Register dest2) {
        this(name, sizeHelper.getDefaultSize(), condition, dest, from, offset, sizeHelper, dest2);
    }

    protected LdrStrBase(String name, Condition condition, Register dest, Register from,
                         Immediate12 offset,
                         SizeHelper<ArmInstruction, Size> sizeHelper, Register dest2) {
        this(name, sizeHelper.getDefaultSize(), condition, dest, from, offset, sizeHelper, dest2);
    }

    protected LdrStrBase(String name, Size size, Register dest, Register from,
                         Register offset,
                         SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, size, Condition.AL, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(String name, Size size, Register dest, Register from,
                         Immediate12 offset,
                         SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, size, Condition.AL, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(String name, Register dest, Register from, Register offset,
                         SizeHelper<ArmInstruction, Size> sizeHelper, Register dest2) {
        this(name, sizeHelper.getDefaultSize(), Condition.AL, dest, from, offset, sizeHelper, dest2);
    }

    protected LdrStrBase(String name, Register dest, Register from, Immediate12 offset,
                         SizeHelper<ArmInstruction, Size> sizeHelper, Register dest2) {
        this(name, sizeHelper.getDefaultSize(), Condition.AL, dest, from, offset, sizeHelper, dest2);
    }

    protected LdrStrBase(String name, Register dest, Register from, Register offset,
                         SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, Condition.AL, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(String name, Register dest, Register from, Immediate12 offset,
                         SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, Condition.AL, dest, from, offset, sizeHelper, null);
    }

    protected LdrStrBase(String name, Register dest, Register from, SizeHelper<ArmInstruction
            , Size> sizeHelper) {
        this(name, sizeHelper.getDefaultSize(), Condition.AL, dest, from, null, sizeHelper, null);
    }

    protected LdrStrBase(String name, Register dest, Register from, SizeHelper<ArmInstruction
            , Size> sizeHelper,
                         Register dest2) {
        this(name, sizeHelper.getDefaultSize(), Condition.AL, dest, from, null, sizeHelper, dest2);
    }

    protected LdrStrBase(String name, Size size, Register dest, Register from,
                         SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(name, size, Condition.AL, dest, from, null, sizeHelper, null);
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || from.uses(what) || offset.uses(what);
    }

    @Override
    public String generate() {
        StringBuilder sb =
                new StringBuilder(name).append(size).append(" ").append(condition).append(" ")
                        .append(dest.getValue(sizeHelper)).append(", ");

        if (dest2 != null) {
            sb.append(dest2.getValue(sizeHelper)).append(", ");
        }

        sb.append("[").append(from.getValue(sizeHelper));

        if (offset != null) {
            sb.append(", ").append(offset.getValue(sizeHelper));
        }

        return sb.append("]").toString();
    }
}
