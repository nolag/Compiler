package cs444.codegen.instructions.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;


public class Mov extends X86Instruction{
    private final InstructionArg src;
    private final InstructionArg dest;
    private final SizeHelper<X86Instruction, Size> sizeHelper;
    private final Size size;

    public Mov(final NotMemory dest, final InstructionArg src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super(1);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(final Memory dest, final NotMemory src, final Size size, final SizeHelper<X86Instruction, Size> sizeHelper){
        super(1);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(final NotMemory dest, final InstructionArg src, final SizeHelper<X86Instruction, Size> sizeHelper){
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    public Mov(final Memory dest, final NotMemory src, final SizeHelper<X86Instruction, Size> sizeHelper){
        this(dest, src, sizeHelper.getDefaultSize(), sizeHelper);
    }

    @Override
    public String generate() {
        return "mov " + InstructionArg.getSizeStr(size) + " " + dest.getValue(size, sizeHelper) + ", " + src.getValue(size, sizeHelper);
    }
}
