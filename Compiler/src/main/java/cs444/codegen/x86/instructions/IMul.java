package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class IMul extends X86Instruction {
    private final InstructionArg<X86Instruction, Size> arg1;
    private final InstructionArg<X86Instruction, Size> arg2;
    private final SizeHelper<X86Instruction, Size> sizeHelper;
    private final Size size;

    public IMul(InstructionArg<X86Instruction, Size> arg1, SizeHelper<X86Instruction, Size> sizeHelper) {
        this(arg1, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public IMul(InstructionArg<X86Instruction, Size> arg1, Size size, SizeHelper<X86Instruction,
            Size> sizeHelper) {
        //2 and 2-4 are close enough
        super(42, 4);
        this.arg1 = arg1;
        arg2 = null;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public IMul(Register arg1, InstructionArg<X86Instruction, Size> arg2, SizeHelper<X86Instruction
            , Size> sizeHelper) {
        this(arg1, arg2, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public IMul(Register arg1, InstructionArg<X86Instruction, Size> arg2, Size size,
                SizeHelper<X86Instruction, Size> sizeHelper) {
        super(42, 6);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    @Override
    public String generate() {
        if (arg2 == null) {
            return "imul " + arg1.getValue(sizeHelper);
        }
        return "imul " + arg1.getValue(size, sizeHelper) + ", " + arg2.getValue(size, sizeHelper);
    }

    @Override
    public final boolean uses(InstructionArg<X86Instruction, ?> what) {
        return arg1.uses(what) || what == Register.DATA;
    }
}
