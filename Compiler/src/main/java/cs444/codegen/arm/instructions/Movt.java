package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.MovBase;

public class Movt extends MovBase {
    public Movt(Condition cond, Register dest, Operand2 src,
                SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("movt", cond, dest, src, sizeHelper);
    }

    public Movt(Condition cond, Register dest, Immediate16 imm, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        super("movt", cond, dest, imm, sizeHelper);
    }

    public Movt(Condition cond, Register dest, ImmediateStr imm, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        super("movt", cond, dest, imm, sizeHelper);
    }

    public Movt(Register dest, Operand2 src, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("movt", dest, src, sizeHelper);
    }

    public Movt(Register dest, Immediate16 imm, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("movt", dest, imm, sizeHelper);
    }

    public Movt(Register dest, ImmediateStr imm, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("movt", dest, imm, sizeHelper);
    }
}
