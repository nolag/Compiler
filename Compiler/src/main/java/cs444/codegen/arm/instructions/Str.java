package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate12;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.LdrStrBase;

public class Str extends LdrStrBase {
    public Str(Size size, Condition condition, Register dest, Register from,
               Register offset,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", size, condition, dest, from, offset, sizeHelper);
    }

    public Str(Size size, Condition condition, Register dest, Register from,
               Immediate12 offset,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", size, condition, dest, from, offset, sizeHelper);
    }

    public Str(Condition condition, Register dest, Register from, Register offset,
               SizeHelper<ArmInstruction, Size> sizeHelper, Register dest2) {
        super("str", condition, dest, from, offset, sizeHelper, dest2);
    }

    public Str(Condition condition, Register dest, Register from, Immediate12 offset,
               SizeHelper<ArmInstruction, Size> sizeHelper, Register dest2) {
        super("str", condition, dest, from, offset, sizeHelper, dest2);
    }

    public Str(Size size, Register dest, Register from, Register offset,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", size, dest, from, offset, sizeHelper);
    }

    public Str(Size size, Register dest, Register from, Immediate12 offset,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", size, dest, from, offset, sizeHelper);
    }

    public Str(Register dest, Register from, Register offset,
               SizeHelper<ArmInstruction, Size> sizeHelper,
               Register dest2) {
        super("str", dest, from, offset, sizeHelper, dest2);
    }

    public Str(Register dest, Register from, Immediate12 offset,
               SizeHelper<ArmInstruction, Size> sizeHelper,
               Register dest2) {
        super("str", dest, from, offset, sizeHelper, dest2);
    }

    public Str(Register dest, Register from, Register offset, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        super("str", dest, from, offset, sizeHelper);
    }

    public Str(Register dest, Register from, Immediate12 offset, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        super("str", dest, from, offset, sizeHelper);
    }

    public Str(Register dest, Register from, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", dest, from, sizeHelper);
    }

    public Str(Register dest, Register from, SizeHelper<ArmInstruction, Size> sizeHelper,
               Register dest2) {
        super("str", dest, from, sizeHelper, dest2);
    }

    public Str(Size size, Register dest, Register from,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("str", size, dest, from, sizeHelper);
    }
}
