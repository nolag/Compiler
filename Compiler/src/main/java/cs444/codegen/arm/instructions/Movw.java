package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.MovBase;

public class Movw extends MovBase {
    public Movw(Condition cond, Register dest, Operand2 src,
                SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("movw", cond, dest, src, sizeHelper);
    }

    public Movw(Condition cond, Register dest, Immediate16 imm, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        super("movw", cond, dest, imm, sizeHelper);
    }

    public Movw(Condition cond, Register dest, ImmediateStr imm, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        super("movw", cond, dest, imm, sizeHelper);
    }

    public Movw(Register dest, Operand2 src, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("movw", dest, src, sizeHelper);
    }

    public Movw(Register dest, Immediate16 imm, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("movw", dest, imm, sizeHelper);
    }

    public Movw(Register dest, ImmediateStr imm, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("movw", dest, imm, sizeHelper);
    }
}
