package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.X86SizeHelper;

public class Mov extends X86Instruction{
    private final InstructionArg src;
    private final InstructionArg dest;
    private final X86SizeHelper sizeHelper;
    private final Size size;

    public Mov(final NotMemory dest, final InstructionArg src, final Size size, final X86SizeHelper sizeHelper){
        super(1);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(final Memory dest, final NotMemory src, final Size size, final X86SizeHelper sizeHelper){
        super(1);
        this.src = src;
        this.dest = dest;
        this.sizeHelper = sizeHelper;
        this.size = size;
    }

    public Mov(final NotMemory dest, final InstructionArg src, final X86SizeHelper sizeHelper){
        this(dest, src, sizeHelper.defaultStack, sizeHelper);
    }

    public Mov(final Memory dest, final NotMemory src, final X86SizeHelper sizeHelper){
        this(dest, src, sizeHelper.defaultStack, sizeHelper);
    }

    @Override
    public String generate() {
        return "mov " + InstructionArg.getSizeStr(size) + " " + dest.getValue(size, sizeHelper) + ", " + src.getValue(size, sizeHelper);
    }
}
