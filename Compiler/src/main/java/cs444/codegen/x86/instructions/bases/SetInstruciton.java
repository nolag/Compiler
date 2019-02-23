package cs444.codegen.x86.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Size;


public abstract class SetInstruciton extends X86Instruction{
    public final String what;
    public final InstructionArg<X86Instruction, Size> arg;
    public final SizeHelper<X86Instruction, Size> sizeHelper;

    protected SetInstruciton(final String what, final InstructionArg<X86Instruction, Size> arg,
            final SizeHelper<X86Instruction, Size> sizeHelper, final int time, final int size){

        super(time, size);
        this.what = what;
        this.arg = arg;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public final String generate() {
        return what + " " + arg.getValue(sizeHelper.getSize(1), sizeHelper);
    }

    @Override
    public final boolean uses(final InstructionArg<X86Instruction, ?> what) {
        return arg.uses(what);
    }
}
