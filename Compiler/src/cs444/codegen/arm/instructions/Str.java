package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate12;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.LdrStrBase;

public class Str extends LdrStrBase {
    public Str(final Size size, final Condition condition, final Register dest, final Register from, final Register offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", size, condition, dest, from, offset, sizeHelper);
    }

    public Str(final Size size, final Condition condition, final Register dest, final Register from, final Immediate12 offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", size, condition, dest, from, offset, sizeHelper);
    }

    public Str(final Condition condition, final Register dest, final Register from, Register offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper, final Register dest2) {
        super("str", condition, dest, from, offset, sizeHelper, dest2);
    }

    public Str(final Condition condition, final Register dest, final Register from, Immediate12 offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper, final Register dest2) {
        super("str", condition, dest, from, offset, sizeHelper, dest2);
    }

    public Str(final Size size, final Register dest, final Register from, final Register offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", size, dest, from, offset, sizeHelper);
    }

    public Str(final Size size, final Register dest, final Register from, final Immediate12 offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", size, dest, from, offset, sizeHelper);
    }

    public Str(final Register dest, final Register from, Register offset, final SizeHelper<ArmInstruction, Size> sizeHelper,
            final Register dest2) {
        super("str", dest, from, offset, sizeHelper, dest2);
    }

    public Str(final Register dest, final Register from, Immediate12 offset, final SizeHelper<ArmInstruction, Size> sizeHelper,
            final Register dest2) {
        super("str", dest, from, offset, sizeHelper, dest2);
    }

    public Str(final Register dest, final Register from, final Register offset, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", dest, from, offset, sizeHelper);
    }

    public Str(final Register dest, final Register from, final Immediate12 offset, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", dest, from, offset, sizeHelper);
    }

    public Str(final Register dest, final Register from, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", dest, from, sizeHelper);
    }

    public Str(final Register dest, final Register from, final SizeHelper<ArmInstruction, Size> sizeHelper, final Register dest2) {
        super("str", dest, from, sizeHelper, dest2);
    }

    public Str(final Size size, final Register dest, final Register from, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", size, dest, from, sizeHelper);
    }
}
