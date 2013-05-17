package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public abstract class SetInstruciton implements X86Instruction{
    public final String what;
    public final InstructionArg arg;
    public final X86SizeHelper sizeHelper;

    protected SetInstruciton(final String what, final InstructionArg arg, final X86SizeHelper sizeHelper){
        this.what = what;
        this.arg = arg;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public final String generate() {
        return what + " " + arg.getValue(X86SizeHelper.SMALL_SIZE, sizeHelper);
    }
}
