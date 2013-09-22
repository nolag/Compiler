package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class IMul extends X86Instruction {
    private final InstructionArg arg1;
    private final InstructionArg arg2;
    private final SizeHelper<X86Instruction, Size> sizeHelper;
    private final Size size;

    public IMul(final InstructionArg arg1, final SizeHelper<X86Instruction, Size> sizeHelper) {
        this(arg1, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public IMul(final InstructionArg arg1, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        //13-42 and 12-42 is close enough
        super(27);
        this.arg1 = arg1;
        this.arg2 = null;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public IMul(final Register arg1, final InstructionArg arg2, final SizeHelper<X86Instruction, Size> sizeHelper) {
        this(arg1, arg2, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public IMul(final Register arg1, final InstructionArg arg2, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper) {
        //13-42 rounds up to 28
        super(28);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    @Override
    public String generate() {
        if(arg2 == null) return "imul " + arg1.getValue(sizeHelper);
        return "imul " + arg1.getValue(size, sizeHelper) + ", " + arg2.getValue(size, sizeHelper);
    }

    @Override
    public final boolean writesTo(final InstructionArg what) {
        return what == arg1 || what == Register.DATA;
    }
}
