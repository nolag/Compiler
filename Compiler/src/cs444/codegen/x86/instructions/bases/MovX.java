package cs444.codegen.x86.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;


public abstract class MovX extends X86Instruction {
    public final String what;
    public final Register dst;
    public final InstructionArg<X86Instruction, Size>  src;
    public final SizeHelper<X86Instruction, Size> sizeHelper;
    public final Size size;

    public MovX(final String what, final Register dst, final Register src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super(3, 3);
        this.what = what;
        this.dst = dst;
        this.src = src;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public MovX(final String what, final Register dst, final InstructionArg<X86Instruction, Size>  src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        //size is 3-7 so use 7
        super(3, 7);
        this.what = what;
        this.dst = dst;
        this.src = src;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    @Override
    public final String generate() {
        return what + " " + dst.getValue(sizeHelper.getDefaultSize(), sizeHelper) + ", " +
                Size.getSizeStr(size) + " " + src.getValue(size, sizeHelper);
    }

    @Override
    public final boolean uses(final InstructionArg<X86Instruction, ?> what) {
        return dst.uses(what);
    }
}