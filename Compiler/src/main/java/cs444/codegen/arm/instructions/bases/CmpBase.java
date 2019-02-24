package cs444.codegen.arm.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.instructions.InstructionArg;

public abstract class CmpBase extends ArmInstruction {
    private final String name;
    private final Register reg;
    private final Operand2 op2;
    private final Condition cond;
    private final SizeHelper<ArmInstruction, Size> sizeHelper;

    protected CmpBase(String name, Register reg, Operand2 op2, SizeHelper<ArmInstruction,
            Size> sizeHelper,
                      Condition cond) {
        super(0);
        this.name = name;
        this.reg = reg;
        this.op2 = op2;
        this.cond = cond;
        this.sizeHelper = sizeHelper;
    }

    protected CmpBase(String name, Register reg, Operand2 op2, SizeHelper<ArmInstruction,
            Size> sizeHelper) {
        this(name, reg, op2, sizeHelper, Condition.AL);
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return reg.uses(what) || op2.uses(what);
    }

    @Override
    public String generate() {
        return name + cond + " " + reg.getValue(sizeHelper) + ", " + op2.getValue(sizeHelper);
    }
}
