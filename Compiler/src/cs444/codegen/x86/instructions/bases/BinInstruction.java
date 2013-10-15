package cs444.codegen.x86.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public abstract class BinInstruction extends X86Instruction {
    public final String what;
    public final InstructionArg arg1;
    public final InstructionArg arg2;
    public final SizeHelper<X86Instruction, Size> sizeHelper;
    public final Size size;
    public final Size size2;

    protected BinInstruction(final String what, final InstructionArg arg1, final InstructionArg arg2,
final SizeHelper<X86Instruction, Size> sizeHelper, final Size size, final Size size2, final int time, final int instSize) {
        super(time, instSize);
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.sizeHelper = sizeHelper;
        this.size  = size;
        this.size2 = size2;
    }

    protected BinInstruction(final String what, final InstructionArg arg1, final InstructionArg arg2,
            final SizeHelper<X86Instruction, Size> sizeHelper, final Size size, final int time, final int instSize){
        this(what, arg1, arg2, sizeHelper, size, size, time, instSize);
    }

    protected BinInstruction(final String what, final InstructionArg arg1, final InstructionArg arg2,
            final SizeHelper<X86Instruction, Size> sizeHelper, final int time, final int instSize){

        this(what, arg1, arg2, sizeHelper, sizeHelper.getDefaultSize(), sizeHelper.getDefaultSize(), time, instSize);
    }

    @Override
    public final String generate() {
        return what + " " + arg1.getValue(size, sizeHelper) + ", " + arg2.getValue(size2, sizeHelper);
    }

    @Override
    public final boolean uses(final InstructionArg what) {
        return arg1.uses(what) || arg2.uses(what);
    }
}
