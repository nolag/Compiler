package cs444.codegen.x86.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;


public abstract class MovX extends X86Instruction {
    public final String what;
    public final Register dst;
    public final InstructionArg src;
    public final SizeHelper<X86Instruction, Size> sizeHelper;
    public final Size size;

    //NOTE register register is legal, but I don't see why we would want it.
    public MovX(final String what, final Register dst, final InstructionArg src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super(3);
        this.what = what;
        this.dst = dst;
        this.src = src;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    @Override
    public final String generate() {
        return what + " " + dst.getValue(sizeHelper.getDefaultSize(), sizeHelper) + ", " +
                InstructionArg.getSizeStr(size) + " " + src.getValue(size, sizeHelper);
    }

    @Override
    public final boolean writesTo(final InstructionArg what) {
        return what == dst;
    }
}
