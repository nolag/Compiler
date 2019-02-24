package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Mul extends X86Instruction {
    private final InstructionArg<X86Instruction, Size> arg1;
    private final InstructionArg<X86Instruction, Size> arg2;
    private final SizeHelper<X86Instruction, Size> sizeHelper;

    public Mul(InstructionArg<X86Instruction, Size> arg1, SizeHelper<X86Instruction, Size> sizeHelper) {
        super(42, 4);
        this.arg1 = arg1;
        arg2 = null;
        this.sizeHelper = sizeHelper;
    }

    public Mul(Register arg1, InstructionArg<X86Instruction, Size> arg2, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        super(42, 4);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public String generate() {
        if (arg2 == null) {
            return "mul " + arg1.getValue(sizeHelper);
        }
        return "mul " + arg1.getValue(sizeHelper) + ", " + arg2.getValue(sizeHelper);
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        return arg1.uses(what) || what == Register.DATA;
    }
}
