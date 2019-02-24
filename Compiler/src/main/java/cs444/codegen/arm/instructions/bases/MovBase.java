package cs444.codegen.arm.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.*;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.instructions.InstructionPart;

public abstract class MovBase extends ArmInstruction {
    public final boolean s;
    public final Condition cond;
    public final Register dest;
    public final InstructionPart<ArmInstruction, Size> src;

    private final SizeHelper<ArmInstruction, Size> sizeHelper;
    private final String what;

    private MovBase(boolean s, String what, Condition cond, Register dest, InstructionPart<ArmInstruction
            , Size> src,
                    SizeHelper<ArmInstruction, Size> sizeHelper) {
        super(1);
        this.s = s;
        this.cond = cond;
        this.what = what;
        this.dest = dest;
        this.src = src;
        this.sizeHelper = sizeHelper;
    }

    public MovBase(boolean s, String what, Condition cond, Register dest, Operand2 src,
                   SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(s, what, cond, dest, (InstructionPart<ArmInstruction, Size>) src, sizeHelper);
    }

    public MovBase(String what, Condition cond, Register dest, Operand2 src,
                   SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(false, what, cond, dest, (InstructionPart<ArmInstruction, Size>) src, sizeHelper);
    }

    public MovBase(String what, Condition cond, Register dest, Immediate16 imm,
                   SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(false, what, cond, dest, imm, sizeHelper);
    }

    public MovBase(String what, Condition cond, Register dest, ImmediateStr imm,
                   SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(false, what, cond, dest, imm, sizeHelper);
    }

    public MovBase(String what, Register dest, Operand2 src,
                   SizeHelper<ArmInstruction, Size> sizeHelper) {
        this(false, what, Condition.AL, dest, src, sizeHelper);
    }

    public MovBase(String what, Register dest, Immediate16 imm, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        this(false, what, Condition.AL, dest, imm, sizeHelper);
    }

    public MovBase(String what, Register dest, ImmediateStr imm, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        this(false, what, Condition.AL, dest, imm, sizeHelper);
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return dest.uses(what) || src.uses(what);
    }

    @Override
    public String generate() {
        return what + (s ? "s" : "") + cond + " " + dest.getValue(sizeHelper) + ", " + src.getValue(sizeHelper);
    }
}
