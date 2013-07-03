package cs444.codegen.instructions.x86.bases;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public abstract class SetInstruciton extends X86Instruction{
    public final String what;
    public final InstructionArg arg;
    public final X86SizeHelper sizeHelper;

    protected SetInstruciton(final String what, final InstructionArg arg, final X86SizeHelper sizeHelper, final long cost){
        super(cost);
        this.what = what;
        this.arg = arg;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public final String generate() {
        return what + " " + arg.getValue(X86SizeHelper.SMALL_SIZE, sizeHelper);
    }
}
