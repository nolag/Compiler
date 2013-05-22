package cs444.codegen.instructions.x86.bases;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public abstract class MovX extends X86Instruction{
    public final String what;
    public final Register dst;
    public final InstructionArg src;
    public final X86SizeHelper sizeHelper;
    public final Size size;

    //NOTE register register is legal, but I don't see why we would want it.
    public MovX(final String what, final Register dst, final InstructionArg src, final Size size, final X86SizeHelper sizeHelper){
        super(3);
        this.what = what;
        this.dst = dst;
        this.src = src;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    @Override
    public final String generate() {
        return what + " " + dst.getValue(sizeHelper.defaultStack, sizeHelper) + ", " +
                InstructionArg.getSizeStr(size) + " " + src.getValue(size, sizeHelper);
    }
}
