package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Mov extends X86Instruction {
    public final InstructionArg<X86Instruction, Size> src;
    public final InstructionArg<X86Instruction, Size> dest;
    public final SizeHelper<X86Instruction, Size> sizeHelper;
    private final Size size;

    //TODO Note immediate 64 was not used for size, think about this later?

    public Mov(Register dest, Register src, Size size,
               SizeHelper<X86Instruction, Size> sizeHelper) {
        super(1, 2);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(Register dest, Memory src, Size size,
               SizeHelper<X86Instruction, Size> sizeHelper) {
        super(1, 4);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(Register dest, Immediate src, Size size,
               SizeHelper<X86Instruction, Size> sizeHelper) {
        super(1, 3);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(Memory dest, Register src, Size size,
               SizeHelper<X86Instruction, Size> sizeHelper) {
        super(1, src == Register.ACCUMULATOR ? 3 : 4);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(Memory dest, Immediate src, Size size,
               SizeHelper<X86Instruction, Size> sizeHelper) {
        super(1, 6);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(Register dest, Register src, SizeHelper<X86Instruction, Size> sizeHelper) {
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public Mov(Register dest, Memory src, SizeHelper<X86Instruction, Size> sizeHelper) {
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public Mov(Register dest, Immediate src, SizeHelper<X86Instruction, Size> sizeHelper) {
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public Mov(Memory dest, Register src, SizeHelper<X86Instruction, Size> sizeHelper) {
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public Mov(Memory dest, Immediate src, SizeHelper<X86Instruction, Size> sizeHelper) {
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    @Override
    public String generate() {
        return "mov " + Size.getSizeStr(size) + " " + dest.getValue(size, sizeHelper) + ", " + src.getValue(size,
                sizeHelper);
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        return dest.uses(what) || src.uses(what);
    }
}
