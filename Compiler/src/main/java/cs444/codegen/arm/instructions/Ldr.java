package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate12;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.LdrStrBase;

public class Ldr extends LdrStrBase {
    public Ldr(Size size, Condition condition, Register dest, Register from,
               Register offset,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", size, condition, dest, from, offset, sizeHelper);
    }

    public Ldr(Size size, Condition condition, Register dest, Register from,
               Immediate12 offset,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", size, condition, dest, from, offset, sizeHelper);
    }

    public Ldr(Condition condition, Register dest, Register from, Register offset,
               SizeHelper<ArmInstruction, Size> sizeHelper, Register dest2) {
        super("ldr", condition, dest, from, offset, sizeHelper, dest2);
    }

    public Ldr(Condition condition, Register dest, Register from, Immediate12 offset,
               SizeHelper<ArmInstruction, Size> sizeHelper, Register dest2) {
        super("ldr", condition, dest, from, offset, sizeHelper, dest2);
    }

    public Ldr(Size size, Register dest, Register from, Register offset,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", size, dest, from, offset, sizeHelper);
    }

    public Ldr(Size size, Register dest, Register from, Immediate12 offset,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", size, dest, from, offset, sizeHelper);
    }

    public Ldr(Register dest, Register from, Register offset,
               SizeHelper<ArmInstruction, Size> sizeHelper,
               Register dest2) {
        super("ldr", dest, from, offset, sizeHelper, dest2);
    }

    public Ldr(Register dest, Register from, Immediate12 offset,
               SizeHelper<ArmInstruction, Size> sizeHelper,
               Register dest2) {
        super("ldr", dest, from, offset, sizeHelper, dest2);
    }

    public Ldr(Register dest, Register from, Register offset, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        super("ldr", dest, from, offset, sizeHelper);
    }

    public Ldr(Register dest, Register from, Immediate12 offset, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        super("ldr", dest, from, offset, sizeHelper);
    }

    public Ldr(Register dest, Register from, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", dest, from, sizeHelper);
    }

    public Ldr(Register dest, Register from, SizeHelper<ArmInstruction, Size> sizeHelper,
               Register dest2) {
        super("ldr", dest, from, sizeHelper, dest2);
    }

    public Ldr(Size size, Register dest, Register from,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("ldr", size, dest, from, sizeHelper);
    }
}
