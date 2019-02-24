package cs444.codegen.arm.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.bases.CmpBase;

public class Cmn extends CmpBase {

    public Cmn(Register reg, Operand2 op2, SizeHelper<ArmInstruction, Size> sizeHelper) {
        super("cmn", reg, op2, sizeHelper);
    }

    protected Cmn(Register reg, Operand2 op2, SizeHelper<ArmInstruction, Size> sizeHelper,
                  Condition cond) {
        super("cmn", reg, op2, sizeHelper, cond);
    }
}
