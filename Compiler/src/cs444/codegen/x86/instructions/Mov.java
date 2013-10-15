package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Mov extends X86Instruction {
    public final InstructionArg src;
    public final InstructionArg dest;
    private final SizeHelper<X86Instruction, Size> sizeHelper;
    private final Size size;

    //TODO Note immediate 64 was not used for size, think about this later?

    public Mov(final Register dest, final Register src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super(1, 2);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(final Register dest, final Memory src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super(1, 4);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(final Register dest, final Immediate src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super(1, 3);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(final Memory dest, final Register src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super(1, src == Register.ACCUMULATOR ? 3 : 4);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(final Memory dest, final Immediate src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super(1, 6);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(final Register dest, final Register src, final SizeHelper<X86Instruction, Size> sizeHelper){
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public Mov(final Register dest, final Memory src, final SizeHelper<X86Instruction, Size> sizeHelper){
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public Mov(final Register dest, final Immediate src, final SizeHelper<X86Instruction, Size> sizeHelper){
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public Mov(final Memory dest, final Register src, final SizeHelper<X86Instruction, Size> sizeHelper){
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public Mov(final Memory dest, final Immediate src, final SizeHelper<X86Instruction, Size> sizeHelper){
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    @Override
    public String generate() {
        return "mov " + InstructionArg.getSizeStr(size) + " " + dest.getValue(size, sizeHelper) + ", " + src.getValue(size, sizeHelper);
    }

    @Override
    public boolean uses(final InstructionArg what) {
        return dest.uses(what) || src.uses(what);
    }
}
