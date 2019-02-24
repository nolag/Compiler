package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.MovBase;

public class Mov extends MovBase {
    public Mov(boolean s, Condition cond, Register dest, Operand2 src,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super(s, "mov", cond, dest, src, sizeHelper);
    }

    public Mov(Condition cond, Register dest, Operand2 src,
               SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("mov", cond, dest, src, sizeHelper);
    }

    public Mov(Condition cond, Register dest, ImmediateStr imm, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        super("mov", cond, dest, imm, sizeHelper);
    }

    public Mov(Register dest, Operand2 src, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("mov", dest, src, sizeHelper);
    }

    public Mov(Register dest, ImmediateStr imm, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("mov", dest, imm, sizeHelper);
    }
}
