package cs444.codegen.instructions.x86.bases;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.X86SizeHelper;

public abstract class BinInstruction implements X86Instruction{
    public final String what;
    public final InstructionArg arg1;
    public final InstructionArg arg2;
    public final X86SizeHelper sizeHelper;
    public final Size size;

    protected BinInstruction(final String what, final InstructionArg arg1, final InstructionArg arg2,
            final X86SizeHelper sizeHelper, final Size size){

        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.sizeHelper = sizeHelper;
        this.size  = size;
    }

    protected BinInstruction(final String what, final InstructionArg arg1, final InstructionArg arg2, final X86SizeHelper sizeHelper){
        this(what, arg1, arg2, sizeHelper, sizeHelper.defaultStack);
    }

    @Override
    public final String generate() {
        return what + " " + arg1.getValue(size, sizeHelper) + ", " + arg2.getValue(size, sizeHelper);
    }
}
