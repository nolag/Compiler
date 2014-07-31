package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate16;
import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.MovBase;

public class Movw extends MovBase {

    public Movw(final Register dest, final Operand2 src, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("movw", dest, src, sizeHelper);
    }

    public Movw(final Register dest, final Immediate16 imm, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("movw", dest, imm, sizeHelper);
    }

    public Movw(final Register dest, final ImmediateStr imm, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("movw", dest, imm, sizeHelper);
    }
}
