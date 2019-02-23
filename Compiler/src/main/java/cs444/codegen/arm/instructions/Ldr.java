package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate12;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.LdrStrBase;

public class Ldr extends LdrStrBase {
    public Ldr(final Size size, final Condition condition, final Register dest, final Register from, final Register offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", size, condition, dest, from, offset, sizeHelper);
    }

    public Ldr(final Size size, final Condition condition, final Register dest, final Register from, final Immediate12 offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", size, condition, dest, from, offset, sizeHelper);
    }

    public Ldr(final Condition condition, final Register dest, final Register from, Register offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper, final Register dest2) {
        super("ldr", condition, dest, from, offset, sizeHelper, dest2);
    }

    public Ldr(final Condition condition, final Register dest, final Register from, Immediate12 offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper, final Register dest2) {
        super("ldr", condition, dest, from, offset, sizeHelper, dest2);
    }

    public Ldr(final Size size, final Register dest, final Register from, final Register offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", size, dest, from, offset, sizeHelper);
    }

    public Ldr(final Size size, final Register dest, final Register from, final Immediate12 offset,
            final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", size, dest, from, offset, sizeHelper);
    }

    public Ldr(final Register dest, final Register from, Register offset, final SizeHelper<ArmInstruction, Size> sizeHelper,
            final Register dest2) {
        super("ldr", dest, from, offset, sizeHelper, dest2);
    }

    public Ldr(final Register dest, final Register from, Immediate12 offset, final SizeHelper<ArmInstruction, Size> sizeHelper,
            final Register dest2) {
        super("ldr", dest, from, offset, sizeHelper, dest2);
    }

    public Ldr(final Register dest, final Register from, final Register offset, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", dest, from, offset, sizeHelper);
    }

    public Ldr(final Register dest, final Register from, final Immediate12 offset, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", dest, from, offset, sizeHelper);
    }

    public Ldr(final Register dest, final Register from, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", dest, from, sizeHelper);
    }

    public Ldr(final Register dest, final Register from, final SizeHelper<ArmInstruction, Size> sizeHelper, final Register dest2) {
        super("ldr", dest, from, sizeHelper, dest2);
    }

    public Ldr(final Size size, final Register dest, final Register from, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", size, dest, from, sizeHelper);
    }
}
