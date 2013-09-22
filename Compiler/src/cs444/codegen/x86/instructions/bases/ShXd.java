package cs444.codegen.x86.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public abstract class ShXd extends X86Instruction{
    public final String what;
    public final InstructionArg arg1;
    public final InstructionArg arg2;
    public final InstructionArg arg3;
    public final SizeHelper<X86Instruction, Size> sizeHelper;

    protected ShXd(final String what, final InstructionArg arg1, final InstructionArg arg2, final InstructionArg arg3,
            final SizeHelper<X86Instruction, Size> sizeHelper, final long cost){
        super(cost);
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public final String generate() {
        final Size size = sizeHelper.getDefaultSize();
        return what + " " + arg1.getValue(size, sizeHelper) + ", " + arg2.getValue(size, sizeHelper)
                + ", " + arg3.getValue(Size.LOW, sizeHelper);
    }

    @Override
    public final boolean writesTo(final InstructionArg what) {
        return arg1 == what;
    }
}
