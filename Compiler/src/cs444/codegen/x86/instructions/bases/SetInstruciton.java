package cs444.codegen.x86.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;


public abstract class SetInstruciton extends X86Instruction{
    public final String what;
    public final InstructionArg arg;
    public final SizeHelper<X86Instruction, Size> sizeHelper;

    protected SetInstruciton(final String what, final InstructionArg arg, final SizeHelper<X86Instruction, Size> sizeHelper, final long cost){
        super(cost);
        this.what = what;
        this.arg = arg;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public final String generate() {
        return what + " " + arg.getValue(sizeHelper.getSize(1), sizeHelper);
    }

    @Override
    public final boolean writesTo(final InstructionArg what) {
        return arg == what;
    }
}
